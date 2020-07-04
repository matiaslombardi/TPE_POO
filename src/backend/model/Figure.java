package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Figure implements Movable {
    private Color borderColor = Color.BLACK;
    private double borderWidth = 1;
    abstract Point[] getPoints();
    public abstract boolean contains(Point point);

    public Figure(Color borderColor, double borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public void move(double diffX, double diffY) {
        for(Point point : getPoints()){
            point.move(diffX,diffY);
        }
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderColor(Color color){
        borderColor = color;
    }

    public void setBorderWidth(double width){
        borderWidth = width;
    }

    public abstract void drawSelf(GraphicsContext gc);

}
