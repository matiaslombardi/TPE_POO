package backend.model;

import javafx.scene.paint.Color;

public class Circle extends Ellipse {

    public Circle(Color borderColor, double borderWidth, Point centerPoint, double radius) {
        super(borderColor, borderWidth, centerPoint, radius, radius);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), getRadiusX());
    }

}
