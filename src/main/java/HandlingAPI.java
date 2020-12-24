import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class HandlingAPI {

    public void fetchData(Set<Vertex> vertices) {     //w przyszłość dodać jeszcze datę
        for (Vertex v : vertices) {
            convertJSON(v);
        }
    }

    public String getWeatherResponse(double lat, double lng) throws IOException {
        String responseBody;
        String url = "https://api.stormglass.io/v2/weather/point";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", Double.toString(lat));
        parameters.put("lng", Double.toString(lng));
        parameters.put("params", "windSpeed,windDirection");
        parameters.put("source", "sg");

        String query = getParamsString(parameters);
        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Authorization", "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002");
        InputStream response = connection.getInputStream();

        try (Scanner scanner = new Scanner(response)) {
            responseBody = scanner.useDelimiter("\\A").next();
        }
        return responseBody;
    }

    public String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public void convertJSON(Vertex v) {
        try {
            String responseString = getWeatherResponse(v.getX(), v.getY());
            JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
            JsonArray json2 = json1.get("hours").getAsJsonArray();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().create();
            Gson gson = builder.create();
            JsonObject json3;

            for (int i = 0; i < json2.size(); i++) {
                json3 = json2.get(i).getAsJsonObject();
                HourlyData hourlyData = gson.fromJson(json3, HourlyData.class);

                Tuple<Integer, Double> dataTuple = new Tuple(hourlyData.getWindDirection().get("sg"), hourlyData.getWindSpeed().get("sg"));
                v.addWeatherData(dataTuple);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getElevationResponse(double lat, double lng) {
        String responseBody;
        Map<String, String> parameters = new HashMap<>();
        InputStream response = null;

        try {
            String url = "https://api.stormglass.io/v2/elevation/point";
            parameters.put("lat", Double.toString(lat));
            parameters.put("lng", Double.toString(lng));

            String query = getParamsString(parameters);
            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Authorization", "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002");
            response = connection.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (Scanner scanner = new Scanner(Objects.requireNonNull(response))) {
            responseBody = scanner.useDelimiter("\\A").next();
//            System.out.print(responseBody);
        }
        return responseBody;
    }


    public double getElevationFromJSON(Vertex v) {
        String responseString = getElevationResponse(v.getX(), v.getY());
        JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
        JsonObject json2 = json1.get("data").getAsJsonObject();
        String elevation = json2.get("elevation").getAsString();

        return Double.parseDouble(elevation);
    }


    public boolean isWater(Vertex v) {
        double elevation = getElevationFromJSON(v);
        return elevation < 0;
    }


}
