package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Square extends Rectangle {

    public Square(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight);
    }

    @Override
    public String toString() { return String.format("Cuadrado [ %s, %s ]", getTopLeft(), getBottomRight());}

}
