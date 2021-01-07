import java.util.Map;

public class HourlyData {

    private String time;
    private Map<String, Integer> windDirection;
    private Map<String, Double> windSpeed;

    public String getTime() {
        return time;
    }

    public Map<String, Integer> getWindDirection() {
        return windDirection;
    }

    public Map<String, Double> getWindSpeed() {
        return windSpeed;
    }

}
