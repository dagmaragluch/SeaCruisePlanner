import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HandlingAPI {

    public void fetchData(Set<Point> vertices) {     //w przyszłość dodać jeszcze datę
        for (Point p : vertices) {
            convertJSON(p);
        }
    }

    public String getResponse(double lat, double lng) throws IOException {
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
//            System.out.print(responseBody);
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

    public void convertJSON(Point p){
        try {
            String responseString = getResponse(p.getX(), p.getY());
            JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
            JsonArray json2 = json1.get("hours").getAsJsonArray();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().create();
            Gson gson = builder.create();
            JsonObject json3;


            for (int i = 0; i < json2.size(); i++){
                json3 = json2.get(i).getAsJsonObject();
                HourlyData hourlyData = gson.fromJson(json3, HourlyData.class);

                Tuple<Double, Double> dataTuple = new Tuple(hourlyData.getWindDirection().get("sg"), hourlyData.getWindSpeed().get("sg"));
                p.addWeatherData(dataTuple);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
