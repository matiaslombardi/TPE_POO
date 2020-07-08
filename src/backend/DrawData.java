package backend;

public class DrawData {
    private final double firstX, firstY, secondX, secondY;
    public DrawData(double firstX, double firstY, double secondX, double secondY) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.secondX = secondX;
        this.secondY = secondY;
    }
    public double getFirstX() {
        return firstX;
    }
    public double getFirstY() {
        return firstY;
    }
    public double getSecondX() {
        return secondX;
    }
    public double getSecondY() {
        return secondY;
    }
}
