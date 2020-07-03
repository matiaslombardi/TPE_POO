package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Line extends Figure {
    private final Point startPoint, endPoint;

    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    @Override
    Point[] getPoints() {
        return new Point[]{startPoint,endPoint};
    }

    @Override
    public boolean contains(Point point) {
        double slope = (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
        double intercept = startPoint.getY() - slope * startPoint.getX(); // ordenada al origen
        return Double.compare(point.getY(), intercept + slope * point.getX()) == 0;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }
}
