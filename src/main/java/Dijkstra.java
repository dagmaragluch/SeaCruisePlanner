import java.util.*;

public class Dijkstra {
    private Graph graph;

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

}
