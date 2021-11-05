package edu.rit.vexu.pathcreator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class VexPathCreator extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VexPathCreator.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 720);
        stage.setTitle("VEX Path Creator V0.1");
        stage.setScene(scene);
        stage.getIcons().add(new Image(VexPathCreator.class.getResourceAsStream("logo.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}