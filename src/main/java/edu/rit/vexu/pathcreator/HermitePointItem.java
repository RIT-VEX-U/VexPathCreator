package edu.rit.vexu.pathcreator;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Vector;

public class HermitePointItem extends VBox {

    PointItem pointPart;
    HBox vecPart = new HBox();
    TextField angleField = new TextField();
    TextField magField = new TextField();

    private AnchorPane fieldPane;

    public HermitePointItem(int pathIndex, AnchorPane fieldPane)
    {
        this.fieldPane = fieldPane;
        pointPart = new PointItem(pathIndex, fieldPane);
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

    public void addChangeListener(ChangeListener c)
    {
        pointPart.addChangeListener(c);
        magField.textProperty().addListener(c);
        angleField.textProperty().addListener(c);
    }

    public double getDir()
    {
        return Double.parseDouble(angleField.getText());
    }

    public double getMag()
    {
        return Double.parseDouble(magField.getText());
    }

    public boolean isValid() {

        boolean isVecValid = true;
        try
        {
            Double.parseDouble(magField.getText());
            Double.parseDouble(angleField.getText());
        } catch(NumberFormatException e)
        {
            isVecValid = false;
        }

        return pointPart.isValid() && isVecValid;
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
