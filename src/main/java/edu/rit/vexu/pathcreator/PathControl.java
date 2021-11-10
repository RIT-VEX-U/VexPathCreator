package edu.rit.vexu.pathcreator;

import javafx.geometry.Point2D;

public interface PathControl {

    public boolean isValid();

    public Point2D getStartPoint();

    public Point2D getEndPoint();
}
