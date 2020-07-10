package backend.model;

import backend.DrawData;
import backend.Selector;
import javafx.scene.paint.Color;

public class Line extends Figure {
    private static final int DELTA = 1;
    private final Point startPoint, endPoint;
    private final double slope;

    public Line(Color borderColor, double borderWidth, Point pointOne, Point pointTwo) {
        super(borderColor, borderWidth);
        boolean oneIsStart = pointOne.getX() < pointTwo.getX();
        this.startPoint = oneIsStart?pointOne:pointTwo;
        this.endPoint = oneIsStart?pointTwo:pointOne;
        this.slope = (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
    }

    @Override
    Point[] getPoints() {
        return new Point[]{startPoint,endPoint};
    }

    @Override
    public boolean contains(Point point) {
        if( (point.getX() > startPoint.getX() && point.getX() < endPoint.getX()) ) {
            double intercept = startPoint.getY() - slope * startPoint.getX(); // ordenada al origen
            double cmp = point.getY() - intercept - slope * point.getX();
            return cmp > -DELTA && cmp < DELTA;
        }
        return false;
    }

    @Override
    public boolean isContained(Selector selector) {
        return selector.contains(startPoint) && selector.contains(endPoint);
    }

    @Override
    public String toString() {

        return String.format("Línea [ %s , %s ]", startPoint, endPoint);
    }

    @Override
    public DrawData getData() {
        return new DrawData(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }

    @Override
    public boolean isFillable() {
        return false;
    }
}
