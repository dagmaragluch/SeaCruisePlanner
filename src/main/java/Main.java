import java.util.Map;

public class Main {

    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;

    public static String date = "2021-01-10T00:00:00+00:00";
    public static String startPort = "Świnoujście";
    public static String endPort = "Ronne";
    public static String yachtModel = "";



    public static void main(String[] args) {
        Main main = new Main();
        Map<String, Point> ports = PortsReader.readPorts("src/main/resources/ports.txt");

        Point A = ports.get(startPort);
        Point B = ports.get(endPort);

        GraphCreator graphCreator = new GraphCreator(A, B);

        System.out.println("creating graph");
        graphCreator.createGraph();

        System.out.println("FINAL GRAPH:");
        graphCreator.GRAPH.printGraphByIndexes();

        System.out.println("\n Start fetching data from API\n");
        main.handlingAPI.fetchData(graphCreator.GRAPH.getAllVertices());
        System.out.println("end fetching data from API\n");


        start = graphCreator.A;
        Graph graph = graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph);
        runDijkstra();

        System.out.println("\nall vertices count = " + graphCreator.GRAPH.getAllVertices().size());
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
