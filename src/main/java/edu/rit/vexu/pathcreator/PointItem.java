package edu.rit.vexu.pathcreator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A list item and point object on the field.
 * When created, the point will automatically check user inputs, and place a point on the map of the field.
 */
public class PointItem extends HBox {

    private static final int POINT_RADIUS = 8;

    private TextField xText = new TextField();
    private TextField yText = new TextField();

    private Button selBtn = new Button("Select");
    private Button delBtn = new Button("X");

    private Circle fieldGraphic = null;
    private AnchorPane fieldPane;

    /**
     * Create the point
     *
     * @param index What index is this in the list of points
     * @param fieldPane The anchor pane holding the field image, for drawing points
     */
    public PointItem(int index, AnchorPane fieldPane)
    {
        this.fieldPane = fieldPane;
        xText.setPrefWidth(50);
        yText.setPrefWidth(50);

        super.getChildren().add(new Label(" X: "));
        super.getChildren().add(xText);
        super.getChildren().add(new Label(" Y: "));
        super.getChildren().add(yText);

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);

        super.getChildren().add(r);
        super.getChildren().add(selBtn);
        super.getChildren().add(delBtn);

        // When the user enters a new value, check inputs and display it on the map.
        // Currently, only supports integers
        ChangeListener<String> placePointListener = (v, s, t1) ->
        {
            int x = -1, y = -1;
            boolean xValid = true, yValid = true;

            // Check inputs on the X textfield
            try {  x = Integer.parseInt(xText.getText()); }
            catch (NumberFormatException e) { xValid = false; }

            // Check inputs on the Y textfield
            try{ y = Integer.parseInt(yText.getText()); }
            catch(NumberFormatException e) { yValid = false; }

            // Make sure the X isn't larger than the field
            if( ! (x >= 0 && x <= FieldConfig.LOADED_FIELD_WIDTH) )
                xValid = false;

            // Make sure the Y isn't larger than the field
            if( ! (y >= 0 && y <= FieldConfig.LOADED_FIELD_HEIGHT) )
                yValid = false;

            // Change the textfields to RED if they are invalid
            if(!xValid)
                xText.setStyle("-fx-control-inner-background: lightcoral;");
            else
                xText.setStyle("");

            if(!yValid)
                yText.setStyle("-fx-control-inner-background: lightcoral;");
            else
                yText.setStyle("");

            // If everything checks out, add the point to the field.
            // If not, remove the point.
            if(xValid && yValid)
                placePointOnMap(x, y);
            else
                removePointFromMap();
        };

        xText.textProperty().addListener(placePointListener);
        yText.textProperty().addListener(placePointListener);
        fieldPane.heightProperty().addListener((v, n1, n2) -> placePointListener.changed(null, null, ""));
        fieldPane.widthProperty().addListener((v, n1, n2) -> placePointListener.changed(null, null, ""));

        delBtn.addEventHandler(ActionEvent.ACTION, actionEvent -> removePointFromMap());
    }

    /**
     * Externally control what happens when the user clicks the "delete point" button. ("X")
     * Main window will remove the point from the list, and delete the object.
     * OR
     * Path will delete this object AND the rest of the hermite point (in PathItem)
     *
     * @param e Event callback
     */
    public void setDeleteHandler(EventHandler e)
    {
        this.delBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e);
    }


    /**
     * Place or replace a point at X and Y (inches) on the field image
     * @param x inches
     * @param y inches
     */
    public void placePointOnMap(int x, int y)
    {
        // If the circle already exists, remove it and re-add it
        if(fieldGraphic != null || fieldPane.getChildren().contains(fieldGraphic))
            fieldPane.getChildren().remove(fieldGraphic);

        // Place the point on the field, with x/y offset by the pane width (since the pane is larger than the image)
        double paneOffsetX = (fieldPane.getWidth() - FieldConfig.fieldImageScaledWidth) / 2.0;
        double paneOffsetY = (fieldPane.getHeight() - FieldConfig.fieldImageScaledHeight) / 2.0;

        // Scale the "inches" to pixels
        double xPlacement = paneOffsetX + (x * FieldConfig.fieldImageScaledWidth / FieldConfig.LOADED_FIELD_WIDTH);
        double yPlacement = paneOffsetY + (y * FieldConfig.fieldImageScaledHeight / FieldConfig.LOADED_FIELD_HEIGHT);

        fieldGraphic = new Circle(xPlacement, yPlacement, POINT_RADIUS);
        fieldGraphic.fillProperty().set(Color.DARKORANGE);
        fieldGraphic.strokeProperty().set(Color.BLACK);
        fieldPane.getChildren().add(fieldGraphic);
    }

    /**
     * Removes the graphic from the image of the field
     */
    public void removePointFromMap()
    {
        if (fieldGraphic == null || !fieldPane.getChildren().contains(fieldGraphic))
            return;

        fieldPane.getChildren().remove(fieldGraphic);
        fieldGraphic = null;
    }
}
