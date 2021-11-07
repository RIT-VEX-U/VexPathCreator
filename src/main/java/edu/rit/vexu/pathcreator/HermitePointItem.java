package edu.rit.vexu.pathcreator;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HermitePointItem extends VBox {

    PointItem pointPart;
    HBox vecPart = new HBox();
    TextField angleField = new TextField();
    TextField magField = new TextField();

    public HermitePointItem(int pathIndex)
    {
        pointPart = new PointItem(pathIndex);
        angleField.setMaxWidth(50);
        magField.setMaxWidth(50);

        vecPart.getChildren().addAll(
                new Label(" θ: "),
                angleField,
                new Label(" R: "),
                magField
        );

        super.getChildren().addAll(pointPart, vecPart);

    }

    /**
     * Control what happens when the "X" button is pressed for this point.
     * Since PointItem contains the button, pass the handler to that
     *
     * @param e Event callback
     */
    public void setDeleteHandler(EventHandler e)
    {
        pointPart.setDeleteHandler(e);
    }


}
