import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private double x;   //latitude
    private double y;   //longitude
    private int index; //index of vertex
    private List<Tuple<Integer, Double>> weatherData;  //list of weather data for point

    public Vertex(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.weatherData = new ArrayList<>();
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Tuple<Integer, Double>> getWeatherData() {
        return weatherData;
    }

    public Tuple<Integer, Double> getWeatherData(int hour) {
        return weatherData.get(hour);
    }

    public void addWeatherData(Tuple<Integer, Double> hourlyData) {
        this.weatherData.add(hourlyData);
    }

    public String toStringCoordinates() {
        return String.format("(%.6f, %.6f)", this.x, this.y);
    }

    public String toString() {
        return String.valueOf(this.getIndex());
    }

}
