package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Rectangle {

    public Square(Color fillColor, Color borderColor, double borderWidth, Point topLeft, Point bottomRight) {
        super(fillColor, borderColor, borderWidth, topLeft, bottomRight);
    }

    @Override
    public String toString() { return String.format("Cuadrado [ %s, %s ]", getTopLeft(), getBottomRight());}

}
