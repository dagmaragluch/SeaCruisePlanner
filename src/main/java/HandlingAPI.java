import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HandlingAPI {

    public void fetchData(Set<Vertex> vertices) {
        Map<Vertex, CompletableFuture<String>> responses = getAllResponses(vertices);

        for (Map.Entry<Vertex, CompletableFuture<String>> entry : responses.entrySet()) {
            try {
                getWeatherFromJSON(entry.getKey(), entry.getValue().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    public Map<Vertex, URI> createTargetsMap(Set<Vertex> vertices) {
        Map<Vertex, URI> targets = new HashMap<>();
        for (Vertex v : vertices) {
            targets.put(v, createURI(v));
        }
        return targets;
    }

    public Map<Vertex, CompletableFuture<String>> getAllResponses(Set<Vertex> vertices) {
        Map<Vertex, URI> targets = createTargetsMap(vertices);

        HttpClient client = HttpClient.newHttpClient();
        String username = "Authorization";
        String password = "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002";


        Map<Vertex, CompletableFuture<String>> futures =
                targets.entrySet()
                        .stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), client
                                .sendAsync(
                                        HttpRequest.newBuilder(e.getValue()).header(username, password).GET().build(),
                                        HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));

        return futures;
    }


    public static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public void getWeatherFromJSON(Vertex v, String responseString) {
        JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
        JsonArray json2 = json1.get("hours").getAsJsonArray();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().create();
        Gson gson = builder.create();
        JsonObject json3;

        for (int i = 0; i < json2.size(); i++) {
            json3 = json2.get(i).getAsJsonObject();
            HourlyData hourlyData = gson.fromJson(json3, HourlyData.class);

            Tuple<Integer, Double> dataTuple = new Tuple<>(hourlyData.getWindDirection().get("sg"), hourlyData.getWindSpeed().get("sg"));
            v.addWeatherData(dataTuple);
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

    public URI createURI(Vertex v) {
        String url = "https://api.stormglass.io/v2/weather/point";
        URI uri = null;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", Double.toString(v.getX()));
        parameters.put("lng", Double.toString(v.getY()));
        parameters.put("params", "windSpeed,windDirection");
//        if (date != null) {
//            parameters.put("start", date);
//        }
        parameters.put("source", "sg");

        try {
            String query = getParamsString(parameters);
            uri = new URI(url + "?" + query);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


}
