import java.util.*;

public class Dijkstra {
    private Graph graph;
    Delphia47 delphia47 = new Delphia47();

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public Map<Vertex, List<Vertex>> shortestPath(Vertex source) {
        Map<Vertex, Double> distances = new HashMap<>(graph.getVerticesCount());
        HashMap<Vertex, Vertex> predecessors = new HashMap<>(graph.getVerticesCount());
        PriorityQueue<Vertex, Double> queue = new PriorityQueue<>();

        for (Vertex v : graph.getGraph().keySet()) {
            distances.put(v, Double.MAX_VALUE - 1);
        }

        distances.replace(source, 0.0);

        for (Vertex v : graph.getGraph().keySet()) {
            queue.insert(v, distances.get(v));
        }

        while (!queue.empty()) {
            Vertex v = queue.pop();
//            System.out.println(v);
            if (distances.get(v).equals(Double.MAX_VALUE - 1))
                break;

            for (Edge edge : graph.getGraph().get(v)) {
                var edgeEnd = edge.getEnd();

                /************/
                System.out.println("windDirection = " + edge.getEnd().getWeatherData(0).getFirst());
                System.out.println("windSpeed = " + edge.getEnd().getWeatherData(0).getSecond());
                edge.setWeight(calculateEdgeWeight(edge));
                System.out.println("time = " + edge.getWeight());
                System.out.println();
                /************/


                if (distances.get(v) + edge.getWeight() < distances.get(edgeEnd)) {
                    distances.replace(edgeEnd, distances.get(v) + edge.getWeight());
                    predecessors.put(edgeEnd, v);
                    queue.priority(edgeEnd, distances.get(edgeEnd));
                }
            }
        }

        Map<Vertex, List<Vertex>> result = new HashMap<>(graph.getVerticesCount());
//        for (Vertex vertex : distances.keySet()) {

        for (Map.Entry<Vertex, Double> entry : distances.entrySet()) {
            Vertex vertex = entry.getKey();
            List<Vertex> path = new ArrayList<>();
            result.put(vertex, path);
            if (predecessors.get(vertex) != null || vertex.equals(source)) { // check reachability of a vertex
                while (vertex != null) {
                    path.add(vertex);
                    vertex = predecessors.get(vertex);
                }
            }
            Collections.reverse(path);
            System.out.println(entry.getKey().getIndex() + ": " + entry.getValue());
        }
        return result;
    }

    // test - v1
    public double calculateEdgeWeight(Edge edge) {
        double weight = 0.0;

        int windDirection = edge.getEnd().getWeatherData(0).getFirst();
        double windSpeed = edge.getEnd().getWeatherData(0).getSecond();

        double edgeLength = edge.getEdgeLength() * 111;     // [km]
        int alpha = edge.getAlpha();
        int indexWindDirection = windDirectionToIndex(alpha, windDirection);
        double roundedWindSpeed = quantizeWindSpeed(windSpeed);

        double jachtSpeed = delphia47.delphia47.get(roundedWindSpeed)[indexWindDirection];
//        System.out.println("jacht speed = " + jachtSpeed);

        weight = edgeLength / jachtSpeed;
//        System.out.println("time = " + weight);

        return weight;
    }


    /**
     * replace(?) given value to available key data from polar plot
     *
     * @param valueToQuantize - the exact value of wind speed from weather forecast
     * @return quantized value
     */
    public double quantizeWindSpeed(double valueToQuantize) {
        Double[] numbers = delphia47.delphia47.keySet().toArray(new Double[0]);
        Arrays.sort(numbers);

        double d1;
        double d2;

        for (int i = 0; i < numbers.length - 1; i++) {
            d1 = Math.abs(valueToQuantize - numbers[i]);
            d2 = Math.abs(valueToQuantize - numbers[i + 1]);

            if (d1 < d2) {
                return numbers[i];
            }
        }
        return numbers[numbers.length - 1];
    }

    public int windDirectionToIndex(int alpha, int windDirection) {
        int realWindDirection = calculateAngle(alpha, windDirection);

        if (realWindDirection > 180) {      //bo wiatr z lewej i z prawej burty działa tak samo
            realWindDirection = 360 - realWindDirection;
        }
        return (int) Math.round(realWindDirection / 30.0);
    }


    /**
     * @param alpha         - kąt pod jakim płynie jacht względem północy (kąt odchylenia kraqwędzi grafu) - 0 ° oznacza jacht płynący na północ
     * @param windDirection - kąt pod jakim wieje wiatr względem północy (0 ° oznacza wiatr wiejący z północy)
     * @return realAngle - rzeczywisty kąt, pod jakim wiatr działa na jacht
     */
    public int calculateAngle(int alpha, int windDirection) {
        if (alpha <= windDirection) {
            return windDirection - alpha;
        } else {
            return 360 - alpha + windDirection;
        }
    }

}
