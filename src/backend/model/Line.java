package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Figure {
    private final Point startPoint, endPoint;
    private final double slope;

    public Line(Color borderColor, double borderWidth, Point startPoint, Point endPoint) {
        super(borderColor, borderWidth);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.slope = (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
    }

    @Override
    Point[] getPoints() {
        return new Point[]{startPoint,endPoint};
    }

    @Override
    public boolean contains(Point point) {
        double intercept = startPoint.getY() - slope * startPoint.getX(); // ordenada al origen
        double cmp = point.getY() - intercept - slope * point.getX();
        return cmp > -1 && cmp < 1;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }

    @Override
    public String toString() {

        return String.format("Línea [ %s , %s ]", startPoint, endPoint);
    }
}
