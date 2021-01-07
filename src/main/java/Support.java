import java.util.List;
import java.util.Map;

public class Support {

    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;
    private static Vertex end;

    public static String startPort, endPort, yachtModel, date;
    public static int hoursFromStart;

    public Support(String[] input) {
        startPort = input[0];
        endPort = input[1];
        yachtModel = getYachtModel(input[2]);
        date = input[3];
        hoursFromStart = Integer.parseInt(date);
    }


    private String getYachtModel(String yacht) {
        return "src/main/resources/" + yacht + ".csv";
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
        Graph graph = graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph, yachtModel, hoursFromStart);
    }


    public Output runDijkstra() {
        Output output = null;
        var paths = dijkstra.shortestPath(start);

        double time = dijkstra.distances.get(end);

        if (time > Double.MAX_VALUE - 2) {
            return new Output();
        }


        StringBuilder b1 = new StringBuilder();
        double straightDistance = Edge.distance(start, end, "N");
        double realDistance = 0.0;

        List<Vertex> cruisePath = paths.get(end);

//        b1.append(end.getIndex()).append(": ").append(start.getIndex()).append(" ");
        b1.append(start.toStringCoordinates()).append(" ");


        for (int i = 1; i < cruisePath.size(); i++) {
            Vertex v = cruisePath.get(i);
            Vertex prev = cruisePath.get(i - 1);
            b1.append(v.toStringCoordinates()).append(" ");
            realDistance += Edge.distance(prev, v, "N");
        }


//        b1.append("\n");

//        for (int i = 0; i < cruisePath.size(); i++) {
//            Vertex v = cruisePath.get(i);
//            b1.append(v.toStringCoordinates()).append(" ");
//        }

        System.out.println(b1.toString());
//        System.out.println("straightDistance = " + straightDistance + " NM = " + straightDistance * Dijkstra.NAUTICAL_MILE_TO_KILOMETER + " km");
//        System.out.println("realDistance = " + realDistance + " NM = " + realDistance * Dijkstra.NAUTICAL_MILE_TO_KILOMETER + " km");
//        System.out.println("time: " + dijkstra.distances.get(end));


        output = new Output(time, straightDistance, realDistance, b1.toString());

        return output;
    }


}
