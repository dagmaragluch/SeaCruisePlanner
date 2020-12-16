import java.util.*;

public class GraphCreator {

    double latA = 53.94;    //A - Swinoujście
    double lngA = 14.28;
    double latB = 55.09;    //B - Borholm
    double lngB = 14.69;    // odl. - ok. 135 km

    //to test
//    double latA = 10.0;
//    double lngA = 15.0;
//    double latB = 10.0;
//    double lngB = 22.0;


    Vertex A = new Vertex(latA, lngA, 0);
    Point B = new Point(latB, lngB);


    //final double SQRT_3 = Math.sqrt(3);
    final double SQRT_3 = 1.73;
    double a = 0.1;     // dł boku trójkąta równobocznego; w stopniach geogr. !
    double h = a / 2 * SQRT_3;  // wysokość w trójkącie równobocznym; też w stopniach geogr. !
    double H = a * SQRT_3;  // 2 * h; w stopniach geogr. !
    double a_2 = a * 2;
    double areaWidth = 0.5;  // w st. geo; z każdej strony jest tyle
    //    double d = a / 2;   // dokładność, z jaką wyznaczne są punkty - odległość środka trójkąta od wierzchołka
    double d = (2 * a) / SQRT_3;   // dokładność, z jaką wyznaczne są punkty - odległość środka trójkąta od wierzchołka


    Set<Vertex> allVertices = new HashSet<>();
    List<List<Vertex>> graph = new ArrayList<>();

    Point[] vectors = new Point[12];
    Point[] areaBoundary = createArea();


    private int vertexCounter = 0;


    public Point[] fillVectorsArray() {
        vectors[0] = new Point(0.0, H);
        vectors[3] = new Point(a_2, 0.0);
        vectors[6] = new Point(0.0, -H);
        vectors[9] = new Point(-a_2, 0.0);

        vectors[1] = new Point(a, H);
        vectors[5] = new Point(a, -H);
        vectors[7] = new Point(-a, -H);
        vectors[11] = new Point(-a, H);

        vectors[2] = new Point(h * SQRT_3, h);
        vectors[4] = new Point(h * SQRT_3, -h);
        vectors[8] = new Point(-h * SQRT_3, -h);
        vectors[10] = new Point(-h * SQRT_3, h);

        return vectors;
    }

    /**
     * @return 4 points described parallelogram
     */
    public Point[] createArea() {
        Point[] areaBoundary = new Point[4];
        areaBoundary[0] = new Point(latA - areaWidth, lngA);
        areaBoundary[1] = new Point(latA + areaWidth, lngA);
        areaBoundary[2] = new Point(latB + areaWidth, lngB);
        areaBoundary[3] = new Point(latB - areaWidth, lngB);
        return areaBoundary;
    }

    /**
     * @param pointToCheck The point to check
     * @return true if the point is inside the boundary, false otherwise
     */
    public boolean areaContains(Vertex pointToCheck) {   //??
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = areaBoundary.length - 1; i < areaBoundary.length; j = i++) {
            if ((areaBoundary[i].getY() > pointToCheck.getY()) != (areaBoundary[j].getY() > pointToCheck.getY()) &&
                    (pointToCheck.getX() < (areaBoundary[j].getX() - areaBoundary[i].getX()) * (pointToCheck.getY() - areaBoundary[i].getY()) / (areaBoundary[j].getY() - areaBoundary[i].getY()) + areaBoundary[i].getX())) {
                result = !result;
            }
        }
        return result;
    }

    public void createGraph() {
        Stack<Vertex> pointsToCheck = new Stack<>();
        Vertex currentPoint;
        Vertex currentNeighbour;
        vectors = fillVectorsArray();

        //init - add point A
        addNewVertex(A, pointsToCheck);

        while (!pointsToCheck.isEmpty()) {

            // ściągamy pierwszy punkt ze stosu punktów do sprawdzenia
            currentPoint = pointsToCheck.pop();     //pop usuwa i zwraca (peek tylko zwraca) - ściąga ze stosu i nie musimy tego robić na końcu pętli


            for (int i = 0; i < vectors.length; i++) {      //tworzymy sąsiadów wg wektorów "przesunięcia"

                //tworzymy danego sąsiada
                currentNeighbour =
                        new Vertex(currentPoint.getX() + vectors[i].getX(),
                                currentPoint.getY() + vectors[i].getY(), -1);

                if (areaContains(currentNeighbour)) {    //jeśli punkt nalezy do area


                    Vertex alreadyFoundVertex = isPointInVertices(currentNeighbour); // vertex, witch we already found in allVertices

                    if (alreadyFoundVertex == null) {      // point nie ma jeszcze w zbiorze wierzchołków
                        addNewVertex(currentNeighbour, pointsToCheck);
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);

                    } else {                                //point jest już w zbiorze wierzchołków
                        addVerticesToAdjacencyList(currentPoint, alreadyFoundVertex);
                    }
                }
            }
        }
    }


    /**
     * @param startVertex - point, for which we are looking for neighbours
     * @param neighbour   - found neighbour
     */
    public void addVerticesToAdjacencyList(Vertex startVertex, Vertex neighbour) {
        int index = startVertex.getIndex();
        List<Vertex> list = graph.get(index);
        if (!list.contains(neighbour)) {    //żeby uniknąć duplikatów (może zdarzyć się sytuacja, że kilka punktów sprowadzamy do jednego)
            list.add(neighbour);
        }
    }


    /**
     * Add new vertex into graph:
     * - set vertex index
     * - add to allVertices set
     * - create list and add new vertex into it
     *
     * @param newVertex     - vertex to add
     * @param pointsToCheck - stack of vertices don't checked yet
     */
    public void addNewVertex(Vertex newVertex, Stack<Vertex> pointsToCheck) {

        newVertex.setIndex(vertexCounter);     //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
        vertexCounter++;
        allVertices.add(newVertex);

        List<Vertex> list = new LinkedList<>(); //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
        list.add(newVertex);
        graph.add(newVertex.getIndex(), list);

        pointsToCheck.push(newVertex); //dodajemy do stosu punktów do sprawdzenia
    }


    public void printGraph() {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + " --> ");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j).toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printGraphByIndex() {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + " --> ");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j).getIndex() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * @return if given point is already in allVertices set return "identity" point, otherwise return null
     */
    public Vertex isPointInVertices(Vertex pointToCheck) {
        for (Vertex p : allVertices) {
            //if (p.getX() == pointToCheck.getX() && p.getY() == pointToCheck.getY()) {
            if (Math.abs(p.getX() - pointToCheck.getX()) < d && Math.abs(p.getY() - pointToCheck.getY()) < d) { //porównanie nie '==' a co do dokładności d

                // trzeba jeszcze zmienić pointToCheck, żeby uniknąć "duplikatów" punktów - reprezentujemy go  punktem p
                return p;
            }
        }
        return null;
    }


}
