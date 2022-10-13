/*
 * Copyright (c) 2022. XPkg-Client Contributors.
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
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.xpkgclient.exceptions.XPkgFetchException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles getting packages from the remote server.
 */
@UtilityClass
public class Remote extends Thread {

    /**
     * Get all packages from the server.
     *
     * @param cb The callback to execute after downloading all packages from the server. Run within {@link Platform#runLater(Runnable)} so that JavaFX calls can be made from within the new thread.
     * @throws XPkgFetchException Exception thrown if there was an error getting the URL
     */
    @SneakyThrows(MalformedURLException.class)
    public synchronized static void getAllPackages(PackageRetrieveCallback cb) throws IOException, XPkgFetchException {
        ArrayList<Package> packages = new ArrayList<>();

        final URL url = new URL("http://localhost:5020/packages/");
        JSONObject json;
        try {
            json = readJsonFromUrl(url);
        } catch (Throwable e) {
            throw new XPkgFetchException(url, e);
        }

        JSONArray packagesArray = (JSONArray) json.get("data");
        for (Object pkgObj : packagesArray){
            JSONObject pkg = (JSONObject) pkgObj;
            String packageId = pkg.getString("packageId");
            String packageName = pkg.getString("packageName");
            String latestVersion = pkg.getString("latestVersion");
            String authorName = pkg.getString("authorName");
            String description = pkg.getString("description");
            packages.add(new Package(packageId, packageName, latestVersion, description, authorName));
        }

        Platform.runLater(() -> cb.execute(packages));
    }

    /**
     * Read JSON from URL. See <a href="https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java">answer on Stack Overflow</a>.
     *
     * @param url The URL to get the JSON data from.
     * @return the JSON data downloaded from the URL.
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
            System.out.println(jsonText);
            return new JSONObject(jsonText);
        }
    }

    /**
     * This callback is used to run something after downloading a list from remote.
     */
    public interface PackageRetrieveCallback {
        void execute(List<Package> packages);
    }
}
