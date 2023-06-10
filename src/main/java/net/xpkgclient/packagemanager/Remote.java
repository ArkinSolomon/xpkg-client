/*
 * Copyright (c) 2022-2023. Arkin Solomon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.packagemanager;

import javafx.application.Platform;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;
import net.xpkgclient.Configuration;
import net.xpkgclient.versioning.Version;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class handles getting packages from the remote server.
 */
@UtilityClass
public class Remote {

    /**
     * All the packages retrieved from the server. May not be up-to-date.
     *
     * @returns The list of the packages retrieved from the server. Original list, do not modify.
     */
    @Getter
    private List<Package> packages = new ArrayList<>();

    /**
     * Get all packages from the server.
     *
     * @param cb The callback to execute after downloading all packages from the server. Run within {@link Platform#runLater(Runnable)} so that JavaFX calls can be made from within the new thread.
     */
    @SneakyThrows(MalformedURLException.class)
    public synchronized void getAllPackages(PackageRetrieveCallback cb) {
        ArrayList<Package> packages = new ArrayList<>();

        final URL url = new URL("http://localhost:5020/packages/");
        JSONObject json;
        try {
            json = readJsonFromUrl(url);

            JSONArray packagesArray = (JSONArray) json.get("data");
            for (Object pkgObj : packagesArray) {
                JSONObject pkg = (JSONObject) pkgObj;
                String packageId = pkg.getString("packageId");
                String packageName = pkg.getString("packageName");
                String packageTypeStr = pkg.getString("packageType");
                String authorName = pkg.getString("authorName");
                String authorId = pkg.getString("authorId");
                String description = pkg.getString("description");

                JSONArray vJsonArr = pkg.getJSONArray("versions");
                String[] versions = new String[vJsonArr.length()];
                for (int i = 0; i < vJsonArr.length(); ++i)
                    versions[i] = vJsonArr.getString(i);

                PackageType packageType = switch (packageTypeStr) {
                    case "aircraft" -> PackageType.AIRCRAFT;
                    case "executable" -> PackageType.EXECUTABLE;
                    case "other" -> PackageType.OTHER;
                    default ->
                            throw new RuntimeException("Unexpected package type: \"%s\"".formatted(packageTypeStr));
                };

                packages.add(new Package(packageId, packageName, packageType, versions, description, authorName, authorId));
            }

            Remote.packages = packages;
            Platform.runLater(() -> cb.execute(packages));
        } catch (Throwable e) {
            throw new RuntimeException("Could not get all packages from %s".formatted(url), e);
        }
    }

    /**
     * Get a package. Tries to get the most up to date from remote.
     *
     * @param packageId the id of the package to get.
     * @returns The package object of the package. Null if the package is not found.
     */
    public Package getPackage(String packageId) {
        Optional<Package> pkg = packages.stream().filter(p -> p.getPackageId().equals(packageId)).findFirst();
        return pkg.orElse(null);
    }

    /**
     * Download a package.
     *
     * @param pkg        The package to download.
     * @param version    The version of the package to download.
     * @param pkgLocData The package location data of the package, or {@code null} if the package loc data has not yet been downloaded. If it is null the data will be downloaded.
     * @param cb         The callback function which runs after the file has been downloaded and unzipped, with the second parameter as the location of the root of the package downloaded. If there is an exception downloading the package, the file will be null and the first parameter will have the error that caused the download to fail.
     */
    public void downloadPackage(@NotNull Package pkg, Version version, VersionData pkgLocData, FileDownloadedCallback cb) {
        File downloadFile = Path.of(Configuration.getXpPath().getAbsolutePath(), "xpkg", "tmp", "downloads", pkg.getPackageId() + ".xpkg").toFile();

        //noinspection ResultOfMethodCallIgnored
        downloadFile.getParentFile().mkdirs();

        // To prevent callback hell
        CompletableFuture<VersionData> res = new CompletableFuture<>();
        Thread t = new Thread(() -> {
            if (pkgLocData == null)
                getVersionData(pkg.getPackageId(), version, res::complete);
            else
                res.complete(pkgLocData);
        });

        res.thenAccept(data -> {

            if (data.loc.equalsIgnoreCase("NOT_PUBLISHED"))
                throw new RuntimeException("");

            URL packageLoc;
            try (FileOutputStream f = new FileOutputStream(downloadFile);
                 ZipFile zipFile = new ZipFile(downloadFile)) {
                packageLoc = new URL(data.loc);
                ReadableByteChannel zipStream = Channels.newChannel(packageLoc.openStream());
                f.getChannel().transferFrom(zipStream, 0, Long.MAX_VALUE);

                if (!checkFileHash(downloadFile, data.hash))
                    throw new SecurityException("Downloaded file hash does not match expected hash from server");

                File destFile = Path.of(downloadFile.getParentFile().getAbsolutePath(), pkg.getPackageId()).toFile();
                zipFile.extractAll(destFile.getAbsolutePath());

                // We duplicate this code since we don't want any weird behaviour if we decide to do something in the callback (which will execute before any finally block)
                if (downloadFile.exists())
                    //noinspection ResultOfMethodCallIgnored
                    downloadFile.delete();

                Platform.runLater(() -> {
                    try {
                        cb.execute(null, destFile);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Throwable e) {

                if (downloadFile.exists())

                    //noinspection ResultOfMethodCallIgnored
                    downloadFile.delete();
                Platform.runLater(() -> {
                    try {
                        cb.execute(new RuntimeException("Could not download the package %s@%s".formatted(pkg, version), e), null);
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        t.start();
    }

    /**
     * Get the data for a version of a package.
     *
     * @param packageId      The id of the package to get the version data of.
     * @param packageVersion The version of the package to get the version data of.
     * @return The data for the package if it works.
     */
    @SneakyThrows({ExecutionException.class, InterruptedException.class})
    public VersionData getVersionData(String packageId, Version packageVersion) {
        CompletableFuture<Remote.VersionData> res = new CompletableFuture<>();
        Thread t = new Thread(() -> Remote.getVersionData(packageId, packageVersion, res::complete));
        t.start();
        return res.get();
    }

    /**
     * Get the location data for a package asynchronously.
     *
     * @param packageId The id of the package to get the location data of.
     * @param version   The version of the package to get the location data of.
     * @param cb        The callback to execute after getting the location data. Run within {@link Platform#runLater(Runnable)} so that JavaFX calls can be made from within the new thread.
     */
    @SneakyThrows(MalformedURLException.class)
    public void getVersionData(String packageId, @NotNull Version version, VersionDataDownloadedCallback cb) {
        URL url = new URL(String.format("http://localhost:5020/packages/%s/%s", packageId, version));
        JSONObject obj;

        try {
            obj = readJsonFromUrl(url);
        } catch (Throwable e) {
            throw new RuntimeException("Could not fetch the data for %s@%s from remote: %s".formatted(packageId, version, url), e);
        }

        JSONArray dependencyArr = obj.getJSONArray("dependencies");
        JSONArray incompatibilityArr = obj.getJSONArray("incompatibilities");

        List<String[]> dependencies = new LinkedList<>();
        List<String[]> incompatibilities = new LinkedList<>();

        dependencyArr.forEach(dep -> {
            String dependencyId = (String) ((JSONArray) dep).get(0);
            String dependencySelectionStr = (String) ((JSONArray) dep).get(1);
            String[] depMap = new String[]{dependencyId, dependencySelectionStr};
            dependencies.add(depMap);
        });

        incompatibilityArr.forEach(incompatibility -> {
            String incompatibilityId = (String) ((JSONArray) incompatibility).get(0);
            String incompatibilitySelectionStr = (String) ((JSONArray) incompatibility).get(1);
            String[] incompatibilityMap = new String[]{incompatibilityId, incompatibilitySelectionStr};
            incompatibilities.add(incompatibilityMap);
        });

        Platform.runLater(() -> cb.execute(new VersionData(
                obj.getString("loc"),
                obj.getString("hash"),
                dependencies.toArray(String[][]::new),
                incompatibilities.toArray(String[][]::new)
        )));
    }

    /**
     * Check to see if a file's 256sum matches it's expected 256sum. See <a href="https://stackoverflow.com/questions/32032851/how-to-calculate-hash-value-of-a-file-in-java">this answer</a> for where the digest is created, and <a href="https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">chooban's answer</a> to see how to convert the hex using Apache's {@link Hex}.
     *
     * @param fileName     The file to check the 256sum.
     * @param expectedHash The expected file 256sum.
     * @return True if the file's hash checksum the expected checksum.
     * @throws IOException Exception thrown if the file does not exist, or if there was an error reading it.
     */
    @SneakyThrows(NoSuchAlgorithmException.class)
    public boolean checkFileHash(File fileName, String expectedHash) throws IOException {

        byte[] buffer = new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
        while ((count = bis.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }
        bis.close();

        byte[] hash = digest.digest();
        String actualHash = Hex.encodeHexString(hash);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    /**
     * Read JSON from URL. See <a href="https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java">answer on Stack Overflow</a>.
     *
     * @param url The URL to get the JSON data from.
     * @return The JSON data downloaded from the URL.
     * @throws IOException   Exception thrown when there is an issue getting the data from the remote server.
     * @throws JSONException Exception thrown when there is an issue parsing the JSON returned.
     */
    private static @NotNull JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
        try (InputStream is = url.openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1)
                sb.append((char) cp);
            String jsonText = sb.toString();
            return new JSONObject(jsonText);
        }
    }

    /**
     * This callback is used to run something after downloading the list of packages from remote.
     */
    public interface PackageRetrieveCallback {

        /**
         * The callback to execute after downloading the list of packages from remote.
         *
         * @param packages The list of all published and approved packages from remote.
         */
        void execute(List<Package> packages);
    }

    /**
     * Callback used to run something after downloading file from remote.
     */
    public interface FileDownloadedCallback {

        /**
         * The callback to execute after a file has been downloaded from a remote location. The parameter {@code e} is null if the operation successfully, or else {@code e} is the error that caused the download to fail.
         *
         * @param e The error that caused the download to fail.
         * @param f The location of the downloaded file. Is null if e is not null.
         */
        void execute(Throwable e, File f) throws Throwable;
    }

    /**
     * Callback used to run something after getting the data for a version of a package.
     */
    public interface VersionDataDownloadedCallback {

        /**
         * The callback to execute after getting the data for a version of a package.
         *
         * @param data The data returned from the server.
         */
        void execute(VersionData data);
    }

    /**
     * The data returned from the server when looking up a specific package and version.
     *
     * @param loc               The location of the package, or 'NOT_PUBLISHED', if the package version is not published or approved.
     * @param hash              The SHA256 hash of the zip file of the package.
     * @param dependencies      The list of all dependencies of the package as an array of tuples, with the first value being the dependency id, and the second value being the version selection string.
     * @param incompatibilities The list of all incompatibilities of the package as an array of tuples, with the first value being the incompatibility id, and the second value being the version selection string.
     */
    public record VersionData(String loc, String hash,
                              String[][] dependencies,
                              String[][] incompatibilities) {
    }
}
