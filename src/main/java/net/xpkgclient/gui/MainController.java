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

package net.xpkgclient.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.xpkgclient.Configuration;
import net.xpkgclient.Properties;
import net.xpkgclient.packagemanager.Installer;
import net.xpkgclient.packagemanager.Package;
import net.xpkgclient.packagemanager.Remote;
import net.xpkgclient.versioning.Version;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * This class controls the main GUI elements.
 */
public final class MainController {

    private static boolean hasGottenPackages = false;
    private boolean isTreeDisplayed = false;

    @FXML
    private TableView<Package> packageTable;
    @FXML
    private Label statusMessage;
    @FXML
    private Label currentInstallation;

    @FXML
    private Button refreshButton;
    @FXML
    private Button treeViewButton;

    @FXML
    private Button packageDisplayClose;
    @FXML
    private Label packageDisplayName;
    @FXML
    private Label packageDisplayId;
    @FXML
    private TextArea packageDisplayDescription;
    @FXML
    private ChoiceBox<Version> packageDisplaySelector;
    @FXML
    private Button installButton;

    @FXML
    private AnchorPane packageDisplayPane;

    private Package currentPkg;

    /**
     * Initialize the controller.
     */
    @FXML
    public void initialize() {

        setCurrentInstallation(Configuration.getXpPath());
        setPackageDisplayEnabled(false);

        // No idea why it thinks it's type is TableColumn<Package, ?> and won't let me cast the ? to a String
        @SuppressWarnings("unchecked")
        ObservableList<TableColumn<Package, String>> columns = (ObservableList<TableColumn<Package, String>>) ((Object) packageTable.getColumns());
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("packageId"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("packageName"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("latestVersionStr"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("authorName"));
        columns.get(4).setCellValueFactory(new PropertyValueFactory<>("description"));

        packageTable.setRowFactory(tv -> {
            TableRow<Package> row = new TableRow<>();
            row.setOnMouseClicked(event -> updatePackageDisplay(event, row));
            return row;
        });

        installButton.setOnAction(ev -> installCurrentPackage());
        packageDisplayClose.setOnAction(ev -> setPackageDisplayEnabled(false));

        refreshButton.setOnAction(actionEvent -> refreshPackages());
        refreshPackages();

        treeViewButton.setOnAction(actionEvent -> {
            if (isTreeDisplayed)
                return;

            isTreeDisplayed = true;
            DependencyTreeRenderer treeRenderer = new DependencyTreeRenderer(Configuration.getDependencyTree());

            treeRenderer.getFrame().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    isTreeDisplayed = false;
                    super.windowOpened(e);
                }
            });
        });
    }

    /**
     * Set if the package display pane is shown.
     *
     * @param enabled True in order to set if the package display is enabled.
     */
    public void setPackageDisplayEnabled(boolean enabled) {
        packageDisplayPane.setVisible(enabled);
        packageDisplayPane.setManaged(enabled);
    }

    /**
     * Refresh all data from remote.
     */
    public void refreshPackages() {
        setStatus("Downloading packages...");
        setTablePlaceholder("Downloading package information...");

        setAllButtonsEnabled(false);
        packageTable.getItems().clear();

        new Thread(() -> {
            try {
                Remote.getAllPackages(packages -> {
                    packageTable.getItems().setAll(packages);

                    setTablePlaceholder("No packages found");

                    String downloadedPackages = packages.size() + " packages downloaded";
                    if (hasGottenPackages)
                        setStatus(downloadedPackages);
                    else
                        setStatus("Initialized! X-Pkg Client v" + Properties.getVersion() + " [" + downloadedPackages + "]");

                    // We need to duplicate this across the try/catch because if we don't the finally block will run before the status is set, meaning that this if statement becomes useless
                    hasGottenPackages = true;
                    setAllButtonsEnabled(true);
                });
            } catch (Throwable e) {
                Platform.runLater(() -> {
                    e.printStackTrace();
                    setStatus("Could not get packages");
                    setTablePlaceholder("Could not get packages");
                    hasGottenPackages = true;
                    setAllButtonsEnabled(true);
                });
            }
        }).start();
    }

    /**
     * Update the display with a from a click on the table.
     *
     * @param ev  The mouse event of the row that was clicked.
     * @param row The row of the table that was clicked.
     */
    private void updatePackageDisplay(MouseEvent ev, @NotNull TableRow<Package> row) {
        if (!row.isEmpty() && ev.getButton() == MouseButton.PRIMARY
                && ev.getClickCount() == 2) {

            setPackageDisplayEnabled(true);

            currentPkg = row.getItem();
            packageDisplayName.setText(currentPkg.getPackageName());
            packageDisplayId.setText(currentPkg.getPackageId());
            packageDisplayDescription.setText(currentPkg.getDescription());

            ObservableList<Version> versions = FXCollections.observableList(Arrays.stream(currentPkg.getVersions()).toList());
            packageDisplaySelector.setItems(versions);
            packageDisplaySelector.setValue(currentPkg.getLatestVersion());
        }
    }

    /**
     * Install the currently displayed package.
     */
    private synchronized void installCurrentPackage() {
        new Thread(() -> {
            CompletableFuture<Void> installationFuture;
            installationFuture = Installer.installPackage(currentPkg, packageDisplaySelector.getValue());

            try {
                installationFuture.get();
                Platform.runLater(() -> setStatus("Installed package " + currentPkg.getPackageId()));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Change if all the buttons are enabled or disabled.
     *
     * @param enabled True if all the buttons should be enabled, or false otherwise.
     */
    private void setAllButtonsEnabled(boolean enabled) {
        refreshButton.setDisable(!enabled);
    }

    /**
     * Set the text shown briefly in place of the table when downloading packages.
     *
     * @param message The message to show.
     */
    public void setTablePlaceholder(String message) {
        packageTable.setPlaceholder(new Label(message));
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
}
