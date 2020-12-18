import java.io.IOException;

public class Main {

    GraphCreator graphCreator = new GraphCreator();
    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;


    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.graphCreator.createGraph();

        System.out.println("FINAL GRAPH:");
        main.graphCreator.GRAPH.printGraphByIndexes();

        System.out.println("\n Data from API:\n");
        main.handlingAPI.fetchData(main.graphCreator.GRAPH.getAllVertices());
        System.out.println("end fetching data from API:\n");


        start = main.graphCreator.A;
        Graph graph = main.graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph);
        runDijkstra();

        Delphia47 delphia47 = new Delphia47();
//        System.out.println("test = " + delphia47.delphia47.get(12.0)[3]);
    }


    private static void runDijkstra() {
        StringBuilder b = new StringBuilder();
        var paths = dijkstra.shortestPath(start);
        for (var entry : paths.entrySet()) {
            b.append(entry.getKey().getIndex()).append(": ").append(entry.getValue()).append("\n");
        }
        System.err.println("\n");
        System.err.println(b.toString());
    }

}
