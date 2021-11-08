package edu.rit.vexu.pathcreator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow {

    // WINDOW
    @FXML private SplitPane splitPane;

    // MENU BAR
    @FXML private MenuItem openFieldMenuItem;

    // LEFT PANE
    @FXML private ListView<Node> pointList;
    @FXML private Button addPtBtn;
    @FXML private Button addPathBtn;
    @FXML private Button upIndexButton;
    @FXML private Button downIndexButton;

    // RIGHT PANE
    @FXML private AnchorPane imagePane;
    @FXML private ImageView imageView;

    private int numListItems = 0;

    /**
     * Create the window.
     * Anchor the split plane to the window, and auto-resize and auto-center the image to the right-side pane
     */
    @FXML private void initialize() {

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


        ObservableList<Node> list = FXCollections.observableArrayList();
        pointList.setItems(list);

        // Set up the image of the field,
        // Set the FieldImage class to automatically update the image when a new one is chosen
        imageView.setImage(FieldConfig.fieldImage);
        FieldConfig.setUpdateImageCallback( image -> {
            imageView.setImage(image);
            centerFieldImage();
        });

        // Whenever a new point is created, add it to the list and handle the "remove" button correctly
        addPtBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            PointItem pItem = new PointItem(++numListItems, imagePane);
            pItem.setDeleteHandler(event -> list.remove(pItem));
            list.add(pItem);
        });

        // Create a new path, and handle the "remove path" button for each one
        addPathBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            PathItem pItem = new PathItem(imagePane);
            pItem.setDeleteHandler(event -> list.remove(pItem));
            pItem.setSelectedHandler(event -> pointList.getSelectionModel().select(pItem));
            list.add(pItem);
        });

        // When the "up" button is pressed, move the item in the list up, and keep it selected.
        // If nothing is selected, then do nothing.
        upIndexButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int selectedIndex = pointList.getSelectionModel().getSelectedIndex();
            if(selectedIndex == -1 || selectedIndex == 0)
                return;

            // If the item selected is a Path, and the point inside the path are selected,
            // then move the path points around
            if(pointList.getSelectionModel().getSelectedItem() instanceof PathItem)
            {
                PathItem pItem = (PathItem) pointList.getSelectionModel().getSelectedItem();
                if(pItem.moveSelectedHermitePoint(PathItem.Direction.UP))
                    return;
            }

            list.add(selectedIndex - 1, list.remove(selectedIndex));
            pointList.getSelectionModel().select(selectedIndex - 1);
        });

        // When the "down" button is pressed, move the item in the list down, and keep it selected.
        // If nothing is selected, then do nothing.
        downIndexButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int selectedIndex = pointList.getSelectionModel().getSelectedIndex();
            if(selectedIndex == -1 || selectedIndex == (list.size() - 1))
                return;

            // If the item selected is a Path, and the point inside the path are selected,
            // then move the path points around
            if(pointList.getSelectionModel().getSelectedItem() instanceof PathItem)
            {
                PathItem pItem = (PathItem) pointList.getSelectionModel().getSelectedItem();
                if(pItem.moveSelectedHermitePoint(PathItem.Direction.DOWN))
                    return;
            }

            list.add(selectedIndex + 1, list.remove(selectedIndex));
            pointList.getSelectionModel().select(selectedIndex + 1);
        });

        // Open a new field image with options to set the width / length
        openFieldMenuItem.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            try {
                Stage fieldConfigStage = new Stage();
                fieldConfigStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("field-config.fxml"))));
                fieldConfigStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

        FieldConfig.fieldImageScaledWidth = w;
        FieldConfig.fieldImageScaledHeight = h;

        // Offset the image by taking the difference in height / width, and halving it
        imageView.setX((imageView.getFitWidth() - w) / 2.0);
        imageView.setY((imagePane.getHeight() - h) / 2.0);
    }

}