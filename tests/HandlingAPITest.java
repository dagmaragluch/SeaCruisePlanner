import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HandlingAPITest {

    HandlingAPI handlingAPI = new HandlingAPI();
    String jsonTest = """
            {
              "hourlyData": [
                {
                  "time": "2020-12-11T00:00:00+00:00",
                  "windDirection": {
                    "sg": 149
                  },
                  "windSpeed": {
                    "sg": 3.6
                  }
                },
                {
                  "time": "2020-12-11T01:00:00+00:00",
                  "windDirection": {
                    "sg": 149
                  },
                  "windSpeed": {
                    "sg": 3.6
                  }
                }  ],
              "meta": {
                "cost": 1,
                "dailyQuota": 1000,
                "end": "2020-12-21 00:00",
                "lat": 53.94,
                "lng": 14.28,
                "params": [
                  "windSpeed",
                  "windDirection"
                ],
                "requestCount": 1,
                "source": [
                  "sg"
                ],
                "start": "2020-12-11 00:00"
              }
            }""";


    @Test
    public void gsonTest() {
        String jsonString = "{\"x\":\"54.5\", \"y\":21.5}"; //nazwy muszą być tak jak w klasie
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().create();
        Gson gson = builder.create();
        Point point = gson.fromJson(jsonString, Point.class);
        System.out.println(point);

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        System.out.println("lat = " + jsonObject.get("x").getAsString());
        double lng = jsonObject.get("y").getAsDouble();
        System.out.println("lng = " + lng);
    }


    @Test
    public void nestedGsonTest() {
        JsonObject json1 = new JsonParser().parse(jsonTest).getAsJsonObject();
        JsonArray json2 = json1.get("hourlyData").getAsJsonArray();
        JsonObject json3 = json2.get(0).getAsJsonObject();
        JsonElement json4 = json3.get("windSpeed");
        JsonObject json5 = json4.getAsJsonObject();
        JsonElement json6 = json5.get("sg");
        double windSpeed = Double.parseDouble(json6.toString());

        Assert.assertEquals(3.6, windSpeed, 0.0);
    }


    @Test
    public void nestedGsonTest2() {
        JsonObject json1 = new JsonParser().parse(jsonTest).getAsJsonObject();
        JsonArray json2 = json1.get("hourlyData").getAsJsonArray();
        JsonObject json3 = json2.get(0).getAsJsonObject();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().create();
        Gson gson = builder.create();
        HourlyData hourlyData = gson.fromJson(json3, HourlyData.class);

        System.out.println(hourlyData.getTime());
        System.out.println(hourlyData.getWindDirection());
        System.out.println(hourlyData.getWindSpeed());
        System.out.println(hourlyData.getWindDirection().get("sg"));
        System.out.println(hourlyData.getWindSpeed().get("sg"));

        Assert.assertEquals(149.0, hourlyData.getWindDirection().get("sg"), 0.0);
        Assert.assertEquals(3.6, hourlyData.getWindSpeed().get("sg"), 0.0);
    }


    @Test
    public void finalTest() {
        try {
            String responseString = handlingAPI.getWeatherResponse(53.94, 14.28);
            JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
            JsonArray json2 = json1.get("hours").getAsJsonArray();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().create();
            Gson gson = builder.create();
            JsonObject json3;


            for (int i = 0; i < json2.size(); i++) {
                json3 = json2.get(i).getAsJsonObject();
                HourlyData hourlyData = gson.fromJson(json3, HourlyData.class);

                Tuple<Double, Double> dataTuple = new Tuple(hourlyData.getWindDirection().get("sg"), hourlyData.getWindSpeed().get("sg"));
                System.out.println(dataTuple.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void elevationTest() throws IOException {
        double lat = 59.142490;
        double lng = 19.893503;

        String responseBody;
        Map<String, String> parameters = new HashMap<>();
        String url = "https://api.stormglass.io/v2/elevation/point";
        parameters.put("lat", Double.toString(lat));
        parameters.put("lng", Double.toString(lng));

        String query = handlingAPI.getParamsString(parameters);
        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Authorization", "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002");
        InputStream response = connection.getInputStream();

        try (Scanner scanner = new Scanner(response)) {
            responseBody = scanner.useDelimiter("\\A").next();
            System.out.print(responseBody);
        }
    }

    @Test
    public void isWaterTest(){
        Assert.assertTrue(handlingAPI.isWater(new Vertex(55.538211, 18.564577, 0)));   //środek morza
        Assert.assertTrue(handlingAPI.isWater(new Vertex(59.142490, 19.893503, 0)));   //środek morza
        Assert.assertFalse(handlingAPI.isWater(new Vertex(57.095377, 22.083268, 0)));  // Łotwa
        Assert.assertFalse(handlingAPI.isWater(new Vertex(57.422124, 18.475312, 0)));  // Gotlandia
        Assert.assertFalse(handlingAPI.isWater(new Vertex(55.088007, 14.946312, 0)));  // Borholm
        Assert.assertFalse(handlingAPI.isWater(new Vertex(56.552772, 16.519914, 0)));  // okolice Kalmaru, Szwecja
        Assert.assertFalse(handlingAPI.isWater(new Vertex(60.166997, 19.740839, 0)));  // Wyspy Alandzkie
        Assert.assertFalse(handlingAPI.isWater(new Vertex(58.412767, 22.361481, 0)));  // Sarema, Estonia
    }

}
