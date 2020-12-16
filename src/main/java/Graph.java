import java.util.*;

public class Graph {

    private Map<Vertex, List<Edge>> graph;
    private Set<Vertex> allVertices = new HashSet<>();
    private int vertexCounter = 0;


    public Graph(Map<Vertex, List<Edge>> graph){
        this.graph = graph;
    }

    public Map<Vertex, List<Edge>> getGraph() {
        return graph;
    }

    public int getVerticesCount() {
        return graph.size();
    }

    public void increaseVerticesCounter(){
        vertexCounter++;
    }

    public Set<Vertex> getAllVertices() {
        return allVertices;
    }

    public void printGraphByIndexes() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<Vertex, List<Edge>> entry : graph.entrySet()) {
            b.append(entry.getKey().getIndex()).append(" --> ");
            for (Edge e : entry.getValue()) {
                b.append(e.getEnd().getIndex()).append(" ");
            }
            b.append("\n");
        }
        b.append("\n");
        System.out.println(b.toString());
    }

}
