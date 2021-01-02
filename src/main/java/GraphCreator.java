import java.util.*;

public class GraphCreator {

    Vertex A, B;
    double latA, lngA, latB, lngB;
    Point[] areaBoundary;


    public GraphCreator(Point startPort, Point endPort) {
        latA = startPort.getX();
        lngA = startPort.getY();
        latB = endPort.getX();
        lngB = endPort.getY();

        A = new Vertex(latA, lngA, 0);
        B = new Vertex(latB, lngB, -1);

        areaBoundary = createArea();
    }

    //final double SQRT_3 = Math.sqrt(3);
    final double SQRT_3 = 1.73;
    double a = 0.15;     // dł boku trójkąta równobocznego; w stopniach geogr. !
    double h = a / 2 * SQRT_3;  // wysokość w trójkącie równobocznym; też w stopniach geogr. !
    double H = a * SQRT_3;  // 2 * h; w stopniach geogr. !
    double a_2 = a * 2;
    double areaWidth = 0.5;  // w st. geo; z każdej strony jest tyle

    double d = (2 * a) / SQRT_3;   // dokładność, z jaką wyznaczne są punkty - odległość środka trójkąta od wierzchołka

    Graph GRAPH = new Graph(new HashMap<>());

    Map<Vertex, List<Edge>> graph = GRAPH.getGraph();
    Vector[] vectors = new Vector[12];


    HandlingAPI handlingAPI = new HandlingAPI();

    List<Edge> potentialEdges = new ArrayList<>();


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

//    /**
//     * @return 4 points described parallelogram
//     */
//    public Point[] createArea() {
//        Point[] areaBoundary = new Point[4];
//        areaBoundary[0] = new Point(latA, lngA - areaWidth);
//        areaBoundary[1] = new Point(latA, lngA + areaWidth);
//        areaBoundary[2] = new Point(latB, lngB + areaWidth);
//        areaBoundary[3] = new Point(latB, lngB - areaWidth);
//        return areaBoundary;
//    }

    /**
     * @return 4 points described parallelogram
     */
    public Point[] createArea() {

        double x_a = latA;
        double y_a = lngA;
        double x_b = latB;
        double y_b = lngB;
        double d = areaWidth;

        double a = (y_a - y_b) / (x_a - x_b);
        double b_prim_dla_A = y_a + x_a / a;

        double mianownik = a * a + 1;
        double polowa_licznika_dla_A = x_a * a * a + x_a;
        double pierwiastek = Math.sqrt(d * d * a * a * a * a + d * d * a * a);

        double x1 = (polowa_licznika_dla_A - pierwiastek) / mianownik;
        double x2 = (polowa_licznika_dla_A + pierwiastek) / mianownik;

        double y1 = b_prim_dla_A - x1 / a;
        double y2 = b_prim_dla_A - x2 / a;

        double polowa_licznika_dla_B = x_b * a * a + x_b;
        double b_prim_dla_B = y_b + x_b / a;

        double x1_b = (polowa_licznika_dla_B - pierwiastek) / mianownik;
        double x2_b = (polowa_licznika_dla_B + pierwiastek) / mianownik;
        double y1_b = b_prim_dla_B - x1_b / a;
        double y2_b = b_prim_dla_B - x2_b / a;


        Point[] areaBoundary = new Point[4];
        areaBoundary[0] = new Point(x1, y1);
        areaBoundary[1] = new Point(x2, y2);
        areaBoundary[2] = new Point(x1_b, y1_b);
        areaBoundary[3] = new Point(x2_b, y2_b);
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

    /*******************/


    public void createGraphPart1() {
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
                    Edge newEdge;
                    Vertex alreadyFoundVertex = isPointInVertices(currentNeighbour); // vertex, witch we already found in allVertices

                    if (alreadyFoundVertex == null) {      // point nie ma jeszcze w zbiorze wierzchołków
                        newEdge = new Edge(currentPoint, currentNeighbour, i * 30, vectors[i].getLength());
                        addNewVertex(currentNeighbour, pointsToCheck);
                    } else {                                //point jest już w zbiorze wierzchołków
                        newEdge = new Edge(currentPoint, alreadyFoundVertex, i * 30, vectors[i].getLength());
                    }
                    potentialEdges.add(newEdge);
                }
            }
        }
    }


    public void createGraphPart2() {
        Map<Vertex, Boolean> isWaterMap = handlingAPI.fetchElevationData(GRAPH.getAllVertices());

        //to test
//        Map<Vertex, Boolean> isWaterMap = new HashMap<>();
//        for (Vertex v: GRAPH.getAllVertices()) isWaterMap.put(v, true);

        graph.entrySet()
                .removeIf(
                        entry -> (!isWaterMap.get(entry.getKey())));


        GRAPH.getAllVertices().removeIf(v -> !isWaterMap.get(v));


        for (Edge edge : potentialEdges) {
            if (isWaterMap.get(edge.getStart())) { //is valid edge - start and End vertex is water
                if (isWaterMap.get(edge.getEnd()))
                    addEdgeToAdjacencyList(edge);
            }
        }
        addEndPointToGraph();
    }


//    public void createGraph() {
//        createGraphPart1();
//        createGraphPart2();
//    }


    /*********************/
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
                        new Vertex(currentPoint.getX() + vectors[i].getY(),
                                currentPoint.getY() + vectors[i].getX(), -1);
                if (areaContains(currentNeighbour)) {    //jeśli punkt nalezy do area
                    Edge newEdge;
                    Vertex alreadyFoundVertex = isPointInVertices(currentNeighbour); // vertex, witch we already found in allVertices

                    if (alreadyFoundVertex == null) {      // point nie ma jeszcze w zbiorze wierzchołków
//                        if (handlingAPI.isWater(currentNeighbour)) {    //jeśli punkt jest morzem

                        newEdge = new Edge(currentPoint, currentNeighbour, i * 30, vectors[i].getLength());

                        addNewVertex(currentNeighbour, pointsToCheck);
                        addEdgeToAdjacencyList(newEdge);
//                        }

                    } else {                                //point jest już w zbiorze wierzchołków
                        newEdge = new Edge(currentPoint, alreadyFoundVertex, i * 30, vectors[i].getLength());
                        addEdgeToAdjacencyList(newEdge);
                    }
                }
            }
        }

        addEndPointToGraph();   //add point B to graph
    }


    /**
     * @param edge - edge from startVertex to neighbour
     *             startVertex - key vertex
     *             neighbour   - new found vertex
     */
    public void addEdgeToAdjacencyList(Edge edge) {
        Vertex startVertex = edge.getStart();
        Vertex neighbour = edge.getEnd();
        List<Edge> edges = graph.get(startVertex);

        if (!isVertexAlreadyInEdgesList(neighbour, edges)) {
            Objects.requireNonNull(graph.put(startVertex, edges)).add(edge);
        }
    }


    public boolean isVertexAlreadyInEdgesList(Vertex vertexToCheck, List<Edge> edges) {
        for (Edge e : edges) {
            if (e.getEnd().getIndex() == vertexToCheck.getIndex()) {
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

//        newVertex.setIndex(GRAPH.vertexCounter);     //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
        newVertex.setIndex(GRAPH.getVerticesCount());     //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
        GRAPH.increaseVerticesCounter();
        GRAPH.getAllVertices().add(newVertex);

        List<Edge> edges = new ArrayList<>();   //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
        graph.put(newVertex, edges);

        pointsToCheck.push(newVertex); //dodajemy do stosu punktów do sprawdzenia
    }


    /**
     * @return if given point is already in allVertices set return "identity" point, otherwise return null
     */
    public Vertex isPointInVertices(Vertex pointToCheck) {
        for (Vertex p : GRAPH.getAllVertices()) {
            //if (p.getX() == pointToCheck.getX() && p.getY() == pointToCheck.getY()) {
            if (Math.abs(p.getX() - pointToCheck.getX()) < d && Math.abs(p.getY() - pointToCheck.getY()) < d) { //porównanie nie '==' a co do dokładności d

                // trzeba jeszcze zmienić pointToCheck, żeby uniknąć "duplikatów" punktów - reprezentujemy go  punktem p
                return p;
            }
        }
        return null;
    }


    /**
     * add point B to graph
     * if some vertex is close to point B (dist <= a_2) is created edge from this vertex to B,
     * length of this edge is equals to dist, alpha is calculated;
     * in point B doesn't start any edge (B is end point), but B is normal add to graph
     * (because is needed get weather data for point B)
     */
    public void addEndPointToGraph() {
        double dist;
        for (Vertex v : GRAPH.getAllVertices()) {
            dist = Math.sqrt((B.getX() - v.getX()) * (B.getX() - v.getX()) + (B.getY() - v.getY()) * (B.getY() - v.getY()));
            if (dist <= a_2) {
                int alpha = calculateEdgeAlpha(v, B);
                Edge edge = new Edge(v, B, alpha, dist);
                addEdgeToAdjacencyList(edge);
            }
        }
//        B.setIndex(GRAPH.vertexCounter);
        B.setIndex(GRAPH.getVerticesCount());
        GRAPH.getAllVertices().add(B);

        List<Edge> edges = new ArrayList<>();
        graph.put(B, edges);
    }

    /**
     * calculate angle between Oy and PQ - yacht course (relative to the north)
     *
     * @param p - start vertex
     * @param q - end vertex B
     * @return alpha - yacht course in degrees and quantize to 30k
     */
    public int calculateEdgeAlpha(Vertex p, Vertex q) {
        double alpha;
        double a = (q.getY() - p.getY()) / (q.getX() - p.getX());  //współczynnik kierunkowy prostej pq
        double phi = Math.toDegrees(Math.atan(a));  //ponieważ a = tg phi, to phi = arctg(a), ale jest on liczony w radianach i trzeba zamienić jeszcze na stopnie

        if (p.getX() <= q.getX()) {     // kursy 0-180
            alpha = 90.0 - phi;
        } else {                        // kursy 180+
            alpha = 180.0 + 90.0 - phi;
        }
        return (int) Math.round(alpha / 30) * 30;
    }


}
