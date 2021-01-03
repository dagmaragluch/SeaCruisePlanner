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
//        this.weatherData = new ArrayList<>();
        this.weatherData = createWeatherData();
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

    public String toString() {
        return String.format("(%.5f, %.5f)", this.x, this.y);
    }

//    public String toString() {
//        return String.valueOf(this.getIndex());
//    }


    public List createWeatherData(){
        List<Tuple<Integer, Double>> data = new ArrayList<>();

        Tuple<Integer, Double> t1 = new Tuple<>(20, 5.5);
        Tuple<Integer, Double> t2 = new Tuple<>(30, 4.7);
        Tuple<Integer, Double> t3 = new Tuple<>(25, 4.5);

        data.add(t1);
        data.add(t2);
        data.add(t3);

        return data;
    }

}
