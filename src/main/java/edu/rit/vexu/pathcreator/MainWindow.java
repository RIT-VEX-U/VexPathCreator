package edu.rit.vexu.pathcreator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URISyntaxException;

public class MainWindow {

    @FXML
    private ListView<String> pointList;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane imagePane;

    @FXML
    /**
     * Create the window.
     * Anchor the split plane to the window, and auto-resize and auto-center the image to the right-side pane
     */
    private void initialize() throws URISyntaxException {

        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);

        // Match the width and height of the image to the container
        imageView.fitWidthProperty().bind(imagePane.widthProperty());
        imageView.fitHeightProperty().bind(imagePane.heightProperty());

        // Whenever the size of the window changes, recenter the field image
        imagePane.widthProperty().addListener((observableValue, number, t1) -> centerFieldImage());
        imagePane.heightProperty().addListener((observableValue, number, t1) -> centerFieldImage());

    }

    /**
     * Center the image of the field in its parent.
     * By default, the image is bound to 0,0. Set the X and Y by comparing the reference image to the ImageView,
     * and using the difference in aspect ratios.
     */
    public void centerFieldImage() {

        Image i = imageView.getImage();
        double xratio = imageView.getFitWidth() / i.getWidth();
        double yratio = imageView.getFitHeight() / i.getHeight();

        double reduceCoef = 1.0;

        if (xratio > yratio)
            reduceCoef = yratio;
        else
            reduceCoef = xratio;

        // Get the actual size of the image on our screen
        double w = i.getWidth() * reduceCoef;
        double h = i.getHeight() * reduceCoef;

        // Offset the image by taking the difference in height / width, and halving it
        imageView.setX((imageView.getFitWidth() - w) / 2.0);
        imageView.setY((imagePane.getHeight() - h) / 2.0);
    }

}