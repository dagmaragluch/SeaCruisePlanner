import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Main {

    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;
    private static Vertex end;

    public static String date = "2021-01-10T00:00:00+00:00";
//        public static String startPort = "Gdańsk";
//    public static String endPort = "Visby";
//    public static String startPort = "Ronne";
//    public static String endPort = "Świnoujście";

    public static String endPort = "Gdynia";
    public static String startPort = "Sztokholm";
    public static String yachtModel = "bavaria46_cruiser";


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
//        main.handlingAPI.fetchWeatherData(graphCreator.GRAPH.getAllVertices());
        System.out.println("end fetching data from API\n");

        int hoursFromStart = 0;
        String pathToYachtData = "src/main/resources/" + yachtModel + ".csv";

        start = graphCreator.A;
        end = graphCreator.B;
        Graph graph = graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph, pathToYachtData, hoursFromStart);
        runDijkstra();

        System.out.println("\nall vertices count = " + graphCreator.GRAPH.getAllVertices().size());
    }


    private static void runDijkstra() {
        StringBuilder b1 = new StringBuilder();
        StringBuilder b2 = new StringBuilder();
        var paths = dijkstra.shortestPath(start);
        double straightDistance = Edge.distance(start, end, "N");
        double realDistance = 0.0;

        List<Vertex> cruisePath = paths.get(end);

        b1.append(end.getIndex()).append(": ").append(start.getIndex()).append(" ");


        for (int i = 1; i < cruisePath.size(); i++) {
            Vertex v = cruisePath.get(i);
            Vertex prev = cruisePath.get(i - 1);
            b1.append(v.getIndex()).append(" ");
            realDistance += Edge.distance(prev, v, "N");
        }

        for (var entry : paths.entrySet()) {
            b2.append(entry.getKey().getIndex()).append(": ").append(entry.getValue()).append("\n");
        }

        System.out.println("\n");
        System.out.println(b1.toString());
        System.out.println("\n");
        System.out.println("straightDistance = " + straightDistance + " NM = " + straightDistance * Dijkstra.NAUTICAL_MILE_TO_KILOMETER + " km");
        System.out.println("realDistance = " + realDistance + " NM = " + realDistance * Dijkstra.NAUTICAL_MILE_TO_KILOMETER + " km");

        System.err.println("\n");
        System.err.println(b2.toString());
    }


//    private static void daysToStart(String inputData){
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//// Duration oneDay = Duration.between(today, yesterday); // throws an exception
//        long hoursToStart = Duration.between(today.atStartOfDay(), yesterday.atStartOfDay()).toHours();
////        ChronoUnit.DAYS.between()
//    }

}
