public class Main {

    GraphCreator graphCreator = new GraphCreator();


    public static void main(String[] args) {
        Main main = new Main();
        main.graphCreator.createGraph();

        System.out.println("FINAL GRAPH:");
        main.graphCreator.printGraph();

    }
}
