package backend.model;

public abstract class Figure implements Movable {
    abstract Point[] getPoints();
    public abstract boolean contains(Point point);

    @Override
    public void move(double diffX, double diffY) {
        for(Point point : getPoints()){
            point.move(diffX,diffY);
        }
    }
}
