package backend.model;

import backend.DrawData;
import backend.Selector;
import javafx.scene.paint.Color;

public class Rectangle extends FillableFigure {

    private final Point topLeft, bottomRight;

    public Rectangle(Color fillColor, Color borderColor, double borderWidth, Point topLeft, Point bottomRight) {
        super(fillColor, borderColor, borderWidth);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    Point[] getPoints() {
        return new Point[]{topLeft,bottomRight};
    }

    @Override
    public boolean contains(Point point) {
        return point.getX() > topLeft.getX() && point.getX() < bottomRight.getX() &&
                point.getY() > topLeft.getY() && point.getY() < bottomRight.getY();
    }

    @Override
    public boolean isContained(Selector selector) {
        return selector.contains(topLeft) && selector.contains(bottomRight);
    }

    @Override
    public DrawData getData() {
        return new DrawData(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(), bottomRight.getY() - topLeft.getY());
    }
}
