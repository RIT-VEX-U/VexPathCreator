package edu.rit.vexu.pathcreator;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class VexPathCreator extends Application {
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        // Load the game image

        FieldConfig.fieldImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/edu/rit/vexu/pathcreator/tipping_pt.png")));

        FXMLLoader fxmlLoader = new FXMLLoader(VexPathCreator.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 720);
        stage.setTitle("VEX Path Creator V0.1");
        stage.setScene(scene);
        stage.getIcons().add(new Image(VexPathCreator.class.getResourceAsStream("logo.png")));

        // Make sure all windows are closed when exiting the main window
        stage.setOnCloseRequest(windowEvent -> System.exit(0));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}