package edu.rit.vexu.pathcreator;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class PointItem extends HBox {

    private TextField xText = new TextField();
    private TextField yText = new TextField();

    private Button selBtn = new Button("Select");
    private Button delBtn = new Button("X");

    public PointItem(int index)
    {
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
     * Whenever this item's index is changed, run the event passed in.
     * Main window should communicate to every other item and change their indexes, and the list order.
     * @param e 'Value Changed' callback
     */
    public void setIndexChangedEvent(ChangeListener e)
    {
//        orderSpinner.valueProperty().addListener(e);
    }
}
