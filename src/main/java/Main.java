import java.io.IOException;

public class Main {

    GraphCreator graphCreator = new GraphCreator();
    HandlingAPI handlingAPI = new HandlingAPI();


    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.graphCreator.createGraph();

        System.out.println("FINAL GRAPH:");
        main.graphCreator.printGraph();
        System.out.println();
        main.graphCreator.printGraphByIndex();
        System.out.println();
        System.out.println(main.graphCreator.printGraphByIndex2());

        System.out.println("\n Data from API:");
//        main.handlingAPI.fetchData(main.graphCreator.allVertices);

    }
}
