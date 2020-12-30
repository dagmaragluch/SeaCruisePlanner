public class Main {

    GraphCreator graphCreator = new GraphCreator();
    HandlingAPI handlingAPI = new HandlingAPI();
    private static Dijkstra dijkstra;
    private static Vertex start;


    public static void main(String[] args) {
        Main main = new Main();

        System.out.println("creating graph");
        main.graphCreator.createGraph();

        System.out.println("FINAL GRAPH:");
        main.graphCreator.GRAPH.printGraphByIndexes();

        System.out.println("\n Start fetching data from API\n");
        main.handlingAPI.fetchData(main.graphCreator.GRAPH.getAllVertices());
        System.out.println("end fetching data from API\n");


        start = main.graphCreator.A;
        Graph graph = main.graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph);
        runDijkstra();

        System.out.println("\nall vertices count = " + main.graphCreator.GRAPH.getAllVertices().size());
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
