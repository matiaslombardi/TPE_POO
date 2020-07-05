package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;


public abstract class Figure implements Movable {
    private static final int NUM_START = 0;
    private static int idCounter = NUM_START;
    private final int id;
    private static int getAndIncrement(){
        return idCounter++;
    }
    private Color borderColor = Color.BLACK;
    private double borderWidth = 25;

    private Color fillColor = Color.YELLOW;
    abstract Point[] getPoints();
    public abstract boolean contains(Point point);
    public abstract boolean isContained(Rectangle rectangle);

    public Figure(Color fillColor, Color borderColor, double borderWidth) {
        this();
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public Figure() {
        id = getAndIncrement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Figure)) return false;
        Figure figure = (Figure) o;
        return getId() == figure.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getId() {
        return id;
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
