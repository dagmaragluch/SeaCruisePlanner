import java.util.Map;

public class HourlyData {

    private String time;
    private Map<String, Double> windDirection;
    private Map<String, Double> windSpeed;

    public String getTime() {
        return time;
    }

    public Map<String, Double> getWindDirection() {
        return windDirection;
    }

    public Double getWindDirectionValue(String key) {
        return windDirection.get(key);
    }

    public Map<String, Double> getWindSpeed() {
        return windSpeed;
    }

    public Double getWindSpeedValue(String key) {
        return windSpeed.get(key);
    }

}
