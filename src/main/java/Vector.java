public class Vector {

    private final double x;
    private final double y;
    private final double length;

    public Vector(double x, double y, double length) {
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return length;
    }
}
