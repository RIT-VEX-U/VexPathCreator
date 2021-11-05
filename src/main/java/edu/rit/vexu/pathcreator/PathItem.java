package edu.rit.vexu.pathcreator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javax.security.auth.callback.Callback;

/**
 * PathItem
 *
 * Represents a collection of points and vectors that make up a hermite curve.
 * This controls the creation of Path list items on the left-hand side of the screen
 */
public class PathItem extends VBox {

    private static final double MIN_SIZE = 35;
    private static final double CHILD_SIZE = 57;

    static int pathnum = 0;
    private TreeView view;
    private TreeItem root;
    private Button rmPathBtn = new Button("X");
    private Button mkPointBtn = new Button("New Hermite Point");
//    private Spinner<Integer> orderSpinner;

    private int numHermitePoints = 0;

    /**
     * Create the list item
     */
    public PathItem()
    {
        // Create the drop-down portion
        Region rootRgn = new Region();
        HBox.setHgrow(rootRgn, Priority.ALWAYS);
        root = new TreeItem(new HBox(
                new Label("Path " + (++pathnum)),
                rootRgn,
                rmPathBtn
        ));

        // "Add Point" button
        root.getChildren().add(new TreeItem(mkPointBtn));

        view = new TreeView(root);
        view.setMinHeight(MIN_SIZE);
        view.setMaxHeight(MIN_SIZE);
        view.setPrefHeight(MIN_SIZE);

        this.getChildren().add(view);

        // Listen for changes in height (aka expanding the tree), and adjust the height accordingly
        root.expandedProperty().addListener((observableValue, aBoolean, t1) -> resizeTree(t1));

        // Button Controls
        // "New Hermite Point"
        mkPointBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            // Create a new point, add it to the path list,
            // and control what to do when it's deleted
            HermitePointItem hpItem = new HermitePointItem(++numHermitePoints);
            TreeItem hpTreeItem = new TreeItem(hpItem);
            hpItem.setDeleteHandler(e -> {
                root.getChildren().remove(hpTreeItem);
                numHermitePoints--;
                resizeTree(root.isExpanded());
            });
            root.getChildren().add(hpTreeItem);

            resizeTree(root.isExpanded());
        }
        );

        // "X"
        // The rest of the handler is added through setDeleteHandler
        rmPathBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> pathnum--);

    }

    private void resizeTree(boolean isExpanded)
    {
        // If the root has expanded its view, check expand based on the height of the children
        if(isExpanded)
        {
            int height = 0;
            for (Object o : root.getChildren())
                height += CHILD_SIZE;
            view.setPrefHeight(height + MIN_SIZE);
            view.setMaxHeight(height + MIN_SIZE);
        }else
        {
            view.setPrefHeight(MIN_SIZE);
            view.setMaxHeight(MIN_SIZE);
        }
    }

    /**
     * Externally control what happens when the user clicks the "delete path" button.
     * Main window will remove the path from the list, and delete the object.
     *
     * @param e Event callback
     */
    public void setDeleteHandler(EventHandler e)
    {
        this.rmPathBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e);
    }
}
