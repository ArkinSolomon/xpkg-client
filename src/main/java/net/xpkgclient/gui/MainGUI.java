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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.xpkgclient.XPkg;

import java.net.URL;

/**
 * The main application window.
 */
public class MainGUI extends Application {

    /**
     * Start the GUI.
     *
     * @param args The arguments provided to the JAR at start.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Load the GUI.
     *
     * @param stage The stage passed from JavaFX.
     * @throws Exception We can throw any exceptions we need.
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL resource = XPkg.class.getResource("/xpkg-client.fxml");
        assert resource != null;
        Parent root = FXMLLoader.load(resource);

        Scene scene = new Scene(root, 1000, 900);

        stage.setTitle("X-Pkg");
        stage.setScene(scene);
        stage.setMinHeight(585);
        stage.setMinWidth(100);
        stage.show();

        Node rootNode = scene.getRoot();
        Bounds rootBounds = root.getBoundsInLocal();

        double deltaW = stage.getWidth() - rootBounds.getWidth();
        double deltaH = stage.getHeight() - rootBounds.getHeight();
        Bounds prefBounds = getPrefBounds(rootNode);

        stage.setMinWidth(prefBounds.getWidth() + deltaW);
        stage.setMinHeight(prefBounds.getHeight() + deltaH);
    }

    /**
     * Get the preferred bounds of a node. See <a href="https://stackoverflow.com/questions/45905053/how-can-i-prevent-a-window-from-being-too-small-in-javafx"> answer on stack overflow</a>.
     *
     * @param node The node ot get the preferred bounds of.
     * @return The preferred of the node.
     */
    private Bounds getPrefBounds(Node node) {
        double prefWidth;
        double prefHeight;

        Orientation bias = node.getContentBias();
        if (bias == Orientation.HORIZONTAL) {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(prefWidth);
        } else if (bias == Orientation.VERTICAL) {
            prefHeight = node.prefHeight(-1);
            prefWidth = node.prefWidth(prefHeight);
        } else {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(-1);
        }
        return new BoundingBox(0, 0, prefWidth, prefHeight);
    }
}
