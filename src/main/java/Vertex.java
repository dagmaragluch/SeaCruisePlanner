import java.util.LinkedList;
import java.util.List;

public class Vertex {

    private double x;   //latitude
    private double y;   //longitude
    private int index; //index of vertex
    private List<Tuple<Double, Double>> weatherData;  //list of weather data for point


    public Vertex(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Tuple<Double, Double>> getWeatherData() {
        return weatherData;
    }

    public void addWeatherData(Tuple<Double, Double> hourlyData) {
        this.weatherData.add(hourlyData);
    }

    public String toString() {
        return String.format("(%.5f, %.5f)", this.x, this.y);
    }

}
