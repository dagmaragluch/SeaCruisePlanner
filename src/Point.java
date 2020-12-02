public class Point {

    private double x;   //latitude
    private double y;   //longitude
    private int number; //index of vertex


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return "(" + this.x + "; " + this.y + ")";
    }
}
