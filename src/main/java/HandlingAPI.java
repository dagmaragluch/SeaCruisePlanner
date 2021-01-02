import com.google.gson.*;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HandlingAPI {
    // custom executor
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(120))
            .build();

    private final HttpClient httpClient2 = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofMillis(500))
            .build();

    public void fetchWeatherData(Set<Vertex> vertices) {
        Map<Vertex, CompletableFuture<String>> responses = getAllResponses(vertices, true);

        for (Map.Entry<Vertex, CompletableFuture<String>> entry : responses.entrySet()) {
            try {
                getWeatherFromJSON(entry.getKey(), entry.getValue().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Vertex, Boolean> fetchElevationData(Set<Vertex> vertices) {
        Map<Vertex, Boolean> isWaterMap = new HashMap<>();
        Map<Vertex, CompletableFuture<String>> responses = getAllResponses(vertices, false);

        for (Map.Entry<Vertex, CompletableFuture<String>> entry : responses.entrySet()) {
            try {
                isWaterMap.put(entry.getKey(), getElevationFromJSON(entry.getKey(), entry.getValue().get()));

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return isWaterMap;
    }


    public Map<Vertex, URI> createTargetsMap(Set<Vertex> vertices, Boolean isWeatherMap) {
        Map<Vertex, URI> targets = new HashMap<>();
        for (Vertex v : vertices) {
            targets.put(v, createURI(v, isWeatherMap));
        }
        return targets;
    }

    public Map<Vertex, CompletableFuture<String>> getAllResponses(Set<Vertex> vertices, Boolean isWeather) {
        Map<Vertex, URI> targets = createTargetsMap(vertices, isWeather);

        String username = "Authorization";
        String password = "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002";


        Map<Vertex, CompletableFuture<String>> futures =
                targets.entrySet()
                        .stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), httpClient
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


    public String getElevationResponse(Vertex v) {
        String username = "Authorization";
        String password = "ec4e364e-1b88-11eb-a5cd-0242ac130002-ec4e36c6-1b88-11eb-a5cd-0242ac130002";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(createURI(v, false))
                .header(username, password)
                .GET()
                .build();

        httpClient2.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        HttpResponse<String> response = null;
        try {
            response = httpClient2.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(response).body();
    }


    public boolean getElevationFromJSON(Vertex v, String responseString) {      //new
//        String responseString = getElevationResponse(v);
        JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
        JsonObject json2 = json1.get("data").getAsJsonObject();
        String elevation = json2.get("elevation").getAsString();

        return Double.parseDouble(elevation) < 0;
    }

    public double getElevationFromJSON(Vertex v) {      //old
        String responseString = getElevationResponse(v);
        JsonObject json1 = new JsonParser().parse(responseString).getAsJsonObject();
        JsonObject json2 = json1.get("data").getAsJsonObject();
        String elevation = json2.get("elevation").getAsString();

        return Double.parseDouble(elevation);
    }


    public boolean isWater(Vertex v) {
        double elevation = getElevationFromJSON(v);
        return elevation < 0;
    }

    public URI createURI(Vertex v, Boolean isWeatherURI) {
        URI uri = null;
        String url;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", Double.toString(v.getX()));
        parameters.put("lng", Double.toString(v.getY()));

        if (isWeatherURI) {
            url = "https://api.stormglass.io/v2/weather/point";
            parameters.put("params", "windSpeed,windDirection");
            parameters.put("source", "sg");
        } else {
            url = "https://api.stormglass.io/v2/elevation/point";
        }

        try {
            String query = getParamsString(parameters);
            uri = new URI(url + "?" + query);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


}
