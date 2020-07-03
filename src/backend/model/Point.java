package backend.model;

public class Point implements Movable {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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
