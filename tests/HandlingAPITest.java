import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
    public void handlerTest() {
        try {
            String json = handlingAPI.getResponse(53.94, 14.28);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            System.out.println(json);
            System.out.println(gson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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


}
