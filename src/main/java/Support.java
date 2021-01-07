import java.util.List;
import java.util.Map;

public class Support {

    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;
    private static Vertex end;
    Graph graph;

    public static String startPort, endPort, yachtModel, date;
    public static int hoursFromStart;

    public Support(String[] input) {
        startPort = input[0];
        endPort = input[1];
        yachtModel = getYachtModel(input[2]);
        date = input[3];
        hoursFromStart = getHoursFromStart(date);
    }


    private String getYachtModel(String yacht) {
        return "src/main/resources/" + yacht + ".csv";
    }

    private int getHoursFromStart(String date) {
        return Integer.parseInt(date);
    }

    public void prepareGraph() {
        Map<String, Point> ports = PortsReader.readPorts("src/main/resources/ports.txt");
        Point A = ports.get(startPort);
        Point B = ports.get(endPort);
        GraphCreator graphCreator = new GraphCreator(A, B);
        System.out.println("****  " + startPort + "-" + endPort + "  ****");
        System.out.println("creating graph");
        graphCreator.createGraph();
        System.out.println("FINAL GRAPH:");
        graphCreator.GRAPH.printGraphByIndexes();

        System.out.println("\n Start fetching data from API\n");
        handlingAPI.fetchWeatherData(graphCreator.GRAPH.getAllVertices());
        System.out.println("end fetching data from API\n");

        start = graphCreator.A;
        end = graphCreator.B;
        graph = graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph, yachtModel, hoursFromStart);
    }

    public void updateDijkstra(String yacht, String date) {
        dijkstra = new Dijkstra(graph, getYachtModel(yacht), getHoursFromStart(date));
    }


    public Output runDijkstra() {
        var paths = dijkstra.shortestPath(start);

        double time = dijkstra.distances.get(end);

        if (time > Double.MAX_VALUE - 2) {
            return new Output();
        }

        StringBuilder b1 = new StringBuilder();
        double straightDistance = Edge.distance(start, end, "N");
        double realDistance = 0.0;

        List<Vertex> cruisePath = paths.get(end);

        b1.append(start.toStringCoordinates()).append(" ");

        for (int i = 1; i < cruisePath.size(); i++) {
            Vertex v = cruisePath.get(i);
            Vertex prev = cruisePath.get(i - 1);
            b1.append(v.toStringCoordinates()).append(" ");
            realDistance += Edge.distance(prev, v, "N");
        }

        System.out.println(b1.toString());

        return new Output(time, straightDistance, realDistance, b1.toString());
    }


}
