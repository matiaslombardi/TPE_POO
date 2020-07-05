package backend.model;

import java.util.Objects;

public class Point implements Movable {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.getX(), x) == 0 && Double.compare(point.getY(), y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double distanceTo(Point other){
        double xAxis = Math.abs(other.x - this.x);
        double yAxis = Math.abs(other.y - this.y);
        return Math.sqrt(Math.pow(xAxis,2) + Math.pow(yAxis,2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public void move(double diffX, double diffY) {
        x += diffX;
        y += diffY;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

}
