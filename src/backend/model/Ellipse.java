package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Figure {

    private final double radiusX, radiusY;
    private final Point centerPoint;

    public Ellipse(Point centerPoint, double radiusX, double radiusY) {
        this.centerPoint = centerPoint;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    @Override
    Point[] getPoints() {
        return new Point[]{centerPoint};
    }

    @Override
    public boolean contains(Point point) {
        return Math.pow((centerPoint.getX() - point.getX())/radiusX, 2) + Math.pow((centerPoint.getY() - point.getY())/radiusY, 2) <= 1;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.fillOval(centerPoint.getX() - radiusX, centerPoint.getY() - radiusY, 2*radiusX, 2*radiusY);
        gc.strokeOval(centerPoint.getX() - radiusX, centerPoint.getY() - radiusY, 2*radiusX, 2*radiusY);
    }

    public double getRadiusX() {
        return radiusX;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, Radio X: %.2f, Radio Y: %.2f]", centerPoint, radiusX, radiusY);
    }
}
