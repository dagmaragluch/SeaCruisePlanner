public class Point {

    private double x;   //latitude
    private double y;   //longitude

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
