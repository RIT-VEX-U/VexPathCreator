package edu.rit.vexu.pathcreator;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.function.Consumer;

public class FieldConfig {

    // Store the currently loaded field width and length for global use.
    // This will be set when loading a new field from the "File" menu
    public static int LOADED_FIELD_HEIGHT = 144;
    public static int LOADED_FIELD_WIDTH = 144;

    // Field image defaults to the current year's game
    public static Image fieldImage = null;
    public static double fieldImageScaledHeight = 0.0;
    public static double fieldImageScaledWidth = 0.0;

    // Callback that will update the ImageView when updateImage.accept(image) is run
    private static Consumer<Image> updateImage = null;

    @FXML
    private Pane pane;
    @FXML
    private TextField imagePathField;
    @FXML
    private Button imageChoosePathBtn;
    @FXML
    private TextField fieldLengthField;
    @FXML
    private TextField fieldWidthField;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button finishBtn;

    /**
     * Create the window, and initialize the event handlers for buttons / fields
     */
    @FXML
    public void initialize() {

        // Choose a file with the file explorer, and place it into the image path field
        imageChoosePathBtn.addEventHandler(ActionEvent.ACTION, event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Field Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File chosen = fileChooser.showOpenDialog(pane.getScene().getWindow());
            if (chosen != null && chosen.exists())
                imagePathField.setText(chosen.getPath());
        });

        // Cancelling saves nothing, and closes the window
        cancelBtn.addEventHandler(ActionEvent.ACTION, event -> ((Stage) pane.getScene().getWindow()).close());

        // Disable the Finish button if any of the prompts is empty
        finishBtn.disableProperty().bind(
                Bindings.or(Bindings.or(
                                imagePathField.textProperty().isEmpty(),
                                fieldLengthField.textProperty().isEmpty()),
                        fieldWidthField.textProperty().isEmpty()));

        finishBtn.addEventHandler(ActionEvent.ACTION, event ->
        {
            // Make sure the new Width and Length are integers (representing inches)
            int newLength = 0, newWidth = 0;
            try {
                newLength = Integer.parseInt(fieldLengthField.getText());
                newWidth = Integer.parseInt(fieldWidthField.getText());
            } catch (NumberFormatException e) {
                Alert a = new Alert(Alert.AlertType.NONE, "Field sizes must be integers!", ButtonType.CLOSE);
                a.show();
                return;
            }

            // If the numbers parsed, load them as new accepted field values
            LOADED_FIELD_HEIGHT = newLength;
            LOADED_FIELD_WIDTH = newWidth;

            // Load an image, and make sure the URL provided is correct
            Image newFieldImage = new Image("file:" + imagePathField.getText());
            if (newFieldImage == null || newFieldImage.isError()) {
                Alert a = new Alert(Alert.AlertType.NONE, "Image path is invalid!", ButtonType.CLOSE);
                a.show();
                return;
            }

            // Load the image as the accepted field image, and update the ImageView on the main window
            fieldImage = newFieldImage;
            if (updateImage != null)
                updateImage.accept(fieldImage);


            ((Stage) pane.getScene().getWindow()).close();
        });
    }

    /**
     * Creates a callback that accepts an image, so the ImageView on the main window can update / resize
     * whenever a new image is loaded
     *
     * @param r Callback to update the image
     */
    public static void setUpdateImageCallback(Consumer<Image> r) {
        updateImage = r;
    }

    /**
     * Convert pixels from the anchorpane containing the field image to inches, based on the
     * saved official height of the field (in inches) and the current size of the image.
     * @param pt_in The pixels, referenced to the field image pane
     * @param fieldPane The field image pane
     * @return The new point, in inches
     */
    public static Point2D pixelsToInches(Point2D pt_in, AnchorPane fieldPane)
    {
        double paneOffsetX = (fieldPane.getWidth() - fieldImageScaledWidth) / 2.0;
        double paneOffsetY = (fieldPane.getHeight() - fieldImageScaledHeight) / 2.0;

        double xInches = (LOADED_FIELD_WIDTH / fieldImageScaledWidth) * (pt_in.getX() - paneOffsetX);
        double yInches = (LOADED_FIELD_HEIGHT / fieldImageScaledHeight) * (pt_in.getY() - paneOffsetY);

        return new Point2D(xInches, yInches);
    }

    /**
     * Convert inches on the field to pixels in reference to the field image pane
     * @param pt_in The point in inches
     * @param fieldPane The field image pane
     * @return The point in pixels
     */
    public static Point2D inchesToPixels(Point2D pt_in, AnchorPane fieldPane)
    {
        // Place the point on the field, with x/y offset by the pane width (since the pane is larger than the image)
        double paneOffsetX = (fieldPane.getWidth() - FieldConfig.fieldImageScaledWidth) / 2.0;
        double paneOffsetY = (fieldPane.getHeight() - FieldConfig.fieldImageScaledHeight) / 2.0;

        // Scale the "inches" to pixels
        double xPlacement = paneOffsetX + (pt_in.getX() * FieldConfig.fieldImageScaledWidth / FieldConfig.LOADED_FIELD_WIDTH);
        double yPlacement = paneOffsetY + (pt_in.getY() * FieldConfig.fieldImageScaledHeight / FieldConfig.LOADED_FIELD_HEIGHT);

        return new Point2D(xPlacement, yPlacement);
    }

}
