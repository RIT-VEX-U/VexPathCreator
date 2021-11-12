package edu.rit.vexu.pathcreator;

import javafx.scene.shape.CubicCurve;

/**
 * The HermiteCurve class defines a cubic Hermite parametric curve segment in (x,y) coordinate space.
 *
 * @author Jacob To
 */
public class HermiteCurve extends CubicCurve {
    /**
     * Constructs a Hermite Curve with a start point, start tangent, end point, and end tangent.
     *
     * @param startX The x value of the start point.
     * @param startY The y value of the start point.
     * @param startAngle The angle of the tangent to the curve at the start point in radians.
     * @param startMag The magnitude of the tangent to the curve at the start point.
     * @param endX The x value of the end point.
     * @param endY The y value of the end point.
     * @param endAngle The angle of the tangent to the curve at the end point in radians.
     * @param endMag The magnitude of the tangent to the curve at the end point.
     */
    public HermiteCurve(double startX, double startY, double startAngle, double startMag, double endX, double endY, double endAngle, double endMag) {
        super(startX, startY, startMag * Math.cos(startAngle) / 3 + startX, startMag * Math.sin(startAngle) / 3 + startY,
                -endMag * Math.cos(endAngle) / 3 + endX, -endMag * Math.sin(endAngle) / 3 + endY, endX, endY);
    }

    /**
     * Returns the tangential angle at the start point bounded by -pi and pi.
     * @return The tangential angle at the start point in radians.
     */
    public double getStartAngle() {
        double theta = Math.atan2(getControlY1() - getStartY(), getControlX1() - getStartX());
        return theta - 2 * Math.PI * Math.floor((theta + Math.PI) / (2 * Math.PI));
    }

    /**
     * Returns the tangential magnitude at the start point.
     * @return The tangential magnitude at the start point.
     */
    public double getStartMag() {
        return 3 * Math.sqrt(Math.pow(getStartX() - getControlX1(), 2) + Math.pow(getStartY() - getControlY1(), 2));
    }

    /**
     * Returns the tangential angle at the end point bounded by -pi and pi.
     * @return The tangential angle at the end point in radians.
     */
    public double getEndAngle() {
        double theta = Math.atan2(getControlY2() - getEndY(), getControlX2() - getEndX()) - Math.PI;
        return theta - 2 * Math.PI * Math.floor((theta + Math.PI) / (2 * Math.PI));
    }

    /**
     * Returns the tangential magnitude at the end point.
     * @return The tangential magnitude at the end point.
     */
    public double getEndMag() {
        return 3 * Math.sqrt(Math.pow(getEndX() - getControlX2(), 2) + Math.pow(getEndY() - getControlY2(), 2));
    }

    /**
     * Set the start tangential angle to a value.
     * @param startAngle The desired start tangential angle.
     */
    public void setStartAngle(double startAngle) {
        setControlX1(getStartMag() * Math.cos(startAngle) / 3 + getStartX());
        setControlY1(getStartMag() * Math.sin(startAngle) / 3 + getStartY());
    }

    /**
     * Set the start tangential magnitude to a value.
     * @param startMag The desired start tangential magnitude.
     */
    public void setStartMag(double startMag) {
        setControlX1(startMag * Math.cos(getStartAngle()) / 3 + getStartX());
        setControlY1(startMag * Math.sin(getStartAngle()) / 3 + getStartY());
    }

    /**
     * Set the end tangential angle to a value.
     * @param endAngle The desired start tangential angle.
     */
    public void setEndAngle(double endAngle) {
        setControlX2(-getEndMag() * Math.cos(endAngle) / 3 + getEndX());
        setControlY2(-getEndMag() * Math.sin(endAngle) / 3 + getEndY());
    }

    /**
     * Set the end tangential magnitude to a value.
     * @param endMag The desired end tangential magnitude.
     */
    public void setEndMag(double endMag) {
        setControlX2(-endMag * Math.cos(getEndAngle()) / 3 + getEndX());
        setControlY2(-endMag * Math.sin(getEndAngle()) / 3 + getEndY());
    }

    /**
     * Set the start tangential angle and magnitude to a value.
     *
     * @param startAngle The desired start tangential angle.
     * @param startMag The desired start tangential magnitude.
     */
    public void setStartTangent(double startAngle, double startMag) {
        setControlX1(startMag * Math.cos(startAngle) / 3 + getStartX());
        setControlY1(startMag * Math.sin(startAngle) / 3 + getStartY());
    }

    /**
     * Set the end tangential angle and magnitude to a value.
     *
     * @param endAngle The desired end tangential angle.
     * @param endMag The desired end tangential magnitude.
     */
    public void setEndTangent(double endAngle, double endMag) {
        setControlX2(-endMag * Math.cos(endAngle) / 3 + getEndX());
        setControlY2(-endMag * Math.sin(endAngle) / 3 + getEndY());
    }

    @Override
    public String toString() {
        String result = super.toString();
        return String.format("HermiteCurve%s, startAngle=%f, startMagnitude=%f, endAngle=%f, endMagnitude=%f]",
                result.substring(10, result.length() - 1), getStartAngle(), getEndMag(), getEndAngle(), getEndMag());
    }
}
