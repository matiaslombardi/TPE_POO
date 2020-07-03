package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Square extends Rectangle {

    public Square(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight);
    }

    @Override
    public String toString() { return String.format("Cuadrado [ %s, %s ]", getTopLeft(), getBottomRight());}

    @Override
    public void drawSelf(GraphicsContext gc){
        gc.fillRect(this.getTopLeft().getX(), this.getTopLeft().getY(),
                Math.abs(this.getTopLeft().getX() - this.getBottomRight().getX()), Math.abs(this.getTopLeft().getX() - this.getBottomRight().getX()));
        gc.strokeRect(this.getTopLeft().getX(), this.getTopLeft().getY(),
                Math.abs(this.getTopLeft().getX() - this.getBottomRight().getX()), Math.abs(this.getTopLeft().getX() - this.getBottomRight().getX()));
    }
}
