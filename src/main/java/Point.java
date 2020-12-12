import java.util.LinkedList;
import java.util.List;

public class Point {

    private double x;   //latitude
    private double y;   //longitude
    private int number; //index of vertex
    private List<Tuple<Double, Double>> weatherData;  //list of weather data for point

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.weatherData = new LinkedList<Tuple<Double, Double>>();
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

    public List<Tuple<Double, Double>> getWeatherData() {
        return weatherData;
    }

    public void addWeatherData(Tuple<Double, Double> hourlyData) {
        this.weatherData.add(hourlyData);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
