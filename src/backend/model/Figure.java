package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Figure implements Movable {
    private Color borderColor;
    private double borderWidth;

    private Color fillColor;
    abstract Point[] getPoints();
    public abstract boolean contains(Point point);

    public Figure(Color fillColor, Color borderColor, double borderWidth) {
        this.fillColor = fillColor;
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

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public abstract void drawSelf(GraphicsContext gc);

}
