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

package net.xpkgclient.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.xpkgclient.Configuration;
import net.xpkgclient.Properties;
import net.xpkgclient.packagemanager.Package;
import net.xpkgclient.packagemanager.Remote;

import java.io.File;

public class MainController {

    @FXML
    private TableView<Package> packageTable;

    @FXML
    private Label statusMessage;

    @FXML
    private Label currentInstallation;

    /**
     * Initialize the controller.
     */
    @FXML
    public void initialize() {

        setCurrentInstallation(Configuration.getXpPath());

        // No idea why it thinks it's type is TableColumn<Package, ?> and won't let me cast the ? to a String
        @SuppressWarnings("unchecked")
        ObservableList<TableColumn<Package, String>> columns = (ObservableList<TableColumn<Package, String>>) ((Object) packageTable.getColumns());
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("packageId"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("packageName"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("version"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("author"));
        columns.get(4).setCellValueFactory(new PropertyValueFactory<>("description"));

        packageTable.setPlaceholder(new Label("Downloading package information..."));
        new Thread(() -> Remote.getAllPackages(packages -> {
            for (final Package p : packages)
                addPackage(p);
            setStatus("Initialized! X-Pkg Client v" + Properties.getVersion());
        })).start();


    }

    /**
     * Set the current status.
     *
     * @param newStatus The new status message.
     */
    public void setStatus(String newStatus) {
        statusMessage.setText(newStatus);
    }

    /**
     * Display the current X-Plane installation.
     */
    public void setCurrentInstallation(File installation) {
        if (installation == null)
            currentInstallation.setText("No X-Plane installation found");
        else
            currentInstallation.setText("Current Installation: " + installation.getAbsolutePath());
    }


    /**
     * Add a package to the table view.
     *
     * @param pkg The package to add.
     */
    public void addPackage(Package pkg) {
        packageTable.getItems().add(pkg);
    }
}
