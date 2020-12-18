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

    public Integer getWindDirectionValue(String key) {
        return windDirection.get(key);
    }

    public Map<String, Double> getWindSpeed() {
        return windSpeed;
    }

    public Double getWindSpeedValue(String key) {
        return windSpeed.get(key);
    }

}
