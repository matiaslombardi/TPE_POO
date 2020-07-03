package backend.model;

import javafx.scene.canvas.GraphicsContext;

public abstract class Figure implements Movable {
    abstract Point[] getPoints();
    public abstract boolean contains(Point point);

    @Override
    public void move(double diffX, double diffY) {
        for(Point point : getPoints()){
            point.move(diffX,diffY);
        }
    }

    public abstract void drawSelf(GraphicsContext gc);

}
