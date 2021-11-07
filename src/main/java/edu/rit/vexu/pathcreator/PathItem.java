package edu.rit.vexu.pathcreator;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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

    public enum Direction { UP, DOWN }

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

    /**
     * Add an external handler for whenever the list is clicked, so the "selected" property updates
     * in the main point list window
     * @param e Event callback
     */
    public void setSelectedHandler(EventHandler e)
    {
        view.addEventHandler(MouseEvent.MOUSE_CLICKED, e);
    }

    /**
     * Checks if any hermite points are selected, and moves them up or down in the viewable list.
     * @param dir The direction; UP or DOWN
     * @return true if successful, false if nothing was selected OR the operation failed
     */
    public boolean moveSelectedHermitePoint(Direction dir)
    {
        int selectedIndex = view.getSelectionModel().getSelectedIndex();
        Object selectedItem = view.getSelectionModel().getSelectedItem();

        // The selected index is 1 greater than the root selected index, since the root doesn't include the drop-down item.
        // Use the selected index for telling what's selected, and the root selected index for manipulating the children
        int rootSelectedIndex = -1;
        for (int i = 0; i < root.getChildren().size(); i++)
            if(selectedItem == root.getChildren().get(i))
                rootSelectedIndex = i;

        // If the "selected item" is the root, "add point" button, or something else, then don't try to move it
        if((selectedIndex >= -1 && selectedIndex <= 1) || rootSelectedIndex >= root.getChildren().size() || rootSelectedIndex == -1)
            return false;

        if (dir == Direction.UP && rootSelectedIndex > 1)
        {
            root.getChildren().add(rootSelectedIndex - 1, root.getChildren().remove(rootSelectedIndex));
            view.getSelectionModel().select(selectedIndex - 1);
        }
        else if (dir == Direction.DOWN && rootSelectedIndex < root.getChildren().size() - 1)
        {
            root.getChildren().add(rootSelectedIndex + 1, root.getChildren().remove(rootSelectedIndex));
            view.getSelectionModel().select(selectedIndex + 1);
        }

        return true;
    }

}
