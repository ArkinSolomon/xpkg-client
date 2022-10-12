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
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class handles getting packages from the remote server.
 */
@UtilityClass
public class Remote extends Thread {

    /**
     * This callback is used to run something after downloading a list from remote.
     */
    public interface PackageRetrieveCallback {
        void execute(List<Package> packages);
    }


    /**
     * Get all packages from the server.
     *
     * @param cb The callback to execute after downloading all packages from the server. Run within {@link Platform#runLater(Runnable)} so that JavaFX calls can be made from within the new thread.
     */
    public static void getAllPackages(PackageRetrieveCallback cb)  {
        ArrayList<Package> packages = new ArrayList<>();

        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 100; ++i) {
            String id = UUID.randomUUID().toString();
            packages.add(new Package(id, "Arkin Package" + i, "1.0.0", "A package", "Arkin Solomon"));
        }

        Platform.runLater(() -> cb.execute(packages));
    }
}
