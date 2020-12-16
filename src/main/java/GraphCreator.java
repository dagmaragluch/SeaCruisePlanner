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
    //    double d = a / 2;
    double d = (2 * a) / SQRT_3;   // dokładność, z jaką wyznaczne są punkty - odległość środka trójkąta od wierzchołka


    Set<Vertex> allVertices = new HashSet<>();
    List<List<Vertex>> graph = new ArrayList<>();
    Map<Vertex, List<Edge>> graph2 = new HashMap<>();

    Vector[] vectors = new Vector[12];
    Point[] areaBoundary = createArea();


    private int vertexCounter = 0;
    private int vertexCounter2 = 0;


    public Vector[] fillVectorsArray() {
        vectors[0] = new Vector(0.0, H, H);
        vectors[3] = new Vector(a_2, 0.0, a_2);
        vectors[6] = new Vector(0.0, -H, H);
        vectors[9] = new Vector(-a_2, 0.0, a_2);

        vectors[1] = new Vector(a, H, a_2);
        vectors[5] = new Vector(a, -H, a_2);
        vectors[7] = new Vector(-a, -H, a_2);
        vectors[11] = new Vector(-a, H, a_2);

        vectors[2] = new Vector(h * SQRT_3, h, H);
        vectors[4] = new Vector(h * SQRT_3, -h, H);
        vectors[8] = new Vector(-h * SQRT_3, -h, H);
        vectors[10] = new Vector(-h * SQRT_3, h, H);

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

        //// - init v2
        A.setIndex(vertexCounter2);     //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
        vertexCounter2++;
        allVertices.add(A);
        List<Edge> edges = new ArrayList<>();
        graph2.put(A, edges);


        while (!pointsToCheck.isEmpty()) {

            // ściągamy pierwszy punkt ze stosu punktów do sprawdzenia
            currentPoint = pointsToCheck.pop();     //pop usuwa i zwraca (peek tylko zwraca) - ściąga ze stosu i nie musimy tego robić na końcu pętli


            for (int i = 0; i < vectors.length; i++) {      //tworzymy sąsiadów wg wektorów "przesunięcia"

                //tworzymy danego sąsiada
                currentNeighbour =
                        new Vertex(currentPoint.getX() + vectors[i].getX(),
                                currentPoint.getY() + vectors[i].getY(), -1);

                if (areaContains(currentNeighbour)) {    //jeśli punkt nalezy do area
                    Edge newEdge;
                    Vertex alreadyFoundVertex = isPointInVertices(currentNeighbour); // vertex, witch we already found in allVertices

                    if (alreadyFoundVertex == null) {      // point nie ma jeszcze w zbiorze wierzchołków
                        newEdge = new Edge(currentPoint, currentNeighbour, i * 30, vectors[i].getLength());

                        addNewEdge(currentNeighbour, pointsToCheck, newEdge);
                        addEdgeToAdjacencyList(currentPoint, currentNeighbour, newEdge);


                        addNewVertex(currentNeighbour, pointsToCheck);
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);

                    } else {                                //point jest już w zbiorze wierzchołków
                        newEdge = new Edge(currentPoint, alreadyFoundVertex, i * 30, vectors[i].getLength());
                        addEdgeToAdjacencyList(currentPoint, alreadyFoundVertex, newEdge);

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
     *
     * @param startVertex - key vertex
     * @param neighbour - new found vertex
     * @param newEdge - edge from startVertex to neighbour
     */
    public void addEdgeToAdjacencyList(Vertex startVertex, Vertex neighbour, Edge newEdge) {
        List<Edge> edges = graph2.get(startVertex);

        if (!isVertexAlreadyInEdgesList(neighbour, edges)){
            Objects.requireNonNull(graph2.put(startVertex, edges)).add(newEdge);
        }
    }


    public boolean isVertexAlreadyInEdgesList(Vertex vertexToCheck, List<Edge> edges) {
        for (Edge e : edges) {
            if (e.getEnd() == vertexToCheck) {      //zmienić na index ???
                return true;
            }
        }
        return false;
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

        List<Vertex> list = new ArrayList<>(); //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
        list.add(newVertex);
        graph.add(newVertex.getIndex(), list);

        pointsToCheck.push(newVertex); //dodajemy do stosu punktów do sprawdzenia
    }




    public void addNewEdge(Vertex newVertex, Stack<Vertex> pointsToCheck, Edge newEdge) {

        newVertex.setIndex(vertexCounter2);     //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
        vertexCounter2++;
        allVertices.add(newVertex);

        List<Edge> edges = new ArrayList<>();   //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
        edges.add(newEdge);
        graph2.put(newVertex, edges);

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


    public String printGraphByIndex2() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<Vertex, List<Edge>> entry : graph2.entrySet()) {
            b.append(entry.getKey().getIndex()).append("--> ");
            for (Edge e : entry.getValue()) {
                b.append(e.getEnd().getIndex()).append(" ");
            }
            b.append("\n");
        }
        b.append("\n");
        return b.toString();
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
