package net.xpkgclient.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.xpkgclient.Configuration;
import net.xpkgclient.Properties;
import net.xpkgclient.XPkg;

import java.io.File;
import java.net.URL;

/**
 * The main application window.
 */
public class MainGUI extends Application {

    private static Label status;
    private static Label currentInstallation;

    /**
     * Start the GUI.
     *
     * @param args The arguments provided to the JAR at start.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Set the current status;
     *
     * @param statusMessage The new status message.
     */
    public static void setStatus(String statusMessage) {
        status.setText(statusMessage);
    }

    /**
     * Display the current X-Plane installation.
     */
    public static void setCurrentInstallation(File installation) {
        if (installation == null)
            currentInstallation.setText("No X-Plane installation found");
        else
            currentInstallation.setText("Current Installation: " + installation.getAbsolutePath());
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

        status = (Label) root.lookup("#statusMessage");
        currentInstallation = (Label) root.lookup("#currentInstallation");

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

        postInit();
    }

    /**
     * Code to be run after the stage is initialized.
     */
    private void postInit() {

        setStatus("X-Pkg Client v" + Properties.getVersion());
        setCurrentInstallation(Configuration.getXpPath());
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
