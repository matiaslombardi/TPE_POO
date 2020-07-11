package backend.model;

import backend.ColorProperty;
import backend.Observable;
import backend.Selector;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class Figure extends Observable implements Movable {
    private static final int NUM_START = 0;
    private static int idCounter = NUM_START;
    private static int getAndIncrement(){
        return idCounter++;
    }

    private final int id;
    private double borderWidth;
    private final Map<ColorProperty, Color> colorPropertyMap = new HashMap<>();

    protected abstract Point[] getPoints();
    public abstract boolean contains(Point point);
    public abstract boolean isContained(Selector selector);

    public Figure(Color borderColor, double borderWidth) {
        id = getAndIncrement();
        this.borderWidth = borderWidth;
        colorPropertyMap.put(ColorProperty.BORDER_COLOR, borderColor);
    }

    public void setColorProperty(ColorProperty colorProperty, Color color) {
        colorPropertyMap.put(colorProperty, color);
    }

    public Color getColorProperty(ColorProperty colorProperty) {
        return colorPropertyMap.getOrDefault(colorProperty, Color.BLACK);
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

    public double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(double width){
        borderWidth = width;
    }

    public abstract boolean isFillable();
}
