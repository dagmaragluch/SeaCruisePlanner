import java.util.*;

public class GraphCreator {

//    double latA = 53.94;    //A - Swinoujście
//    double lngA = 14.28;
//    double latB = 55.09;    //B - Borholm
//    double lngB = 14.69;    // odl. - ok. 135 km

    //to test
    double latA = 10.0;    //A - Swinoujście
    double lngA = 15.0;
    double latB = 10.0;    //B - Borholm
    double lngB = 25.0;    // odl. - ok. 135 km


    Point A = new Point(latA, lngA, 0);
    Point B = new Point(latB, lngB);

    // to test
    double a = 1.0;
    double H = a * 2;

    //    double a = 0.2;     // dł boku trójkąta równobocznego; w stopniach geogr. !
    //    double h = a / 2 * Math.sqrt(3);  // wysokość w trójkącie równobocznym; też w stopniach geogr. !
//    double H = a * Math.sqrt(3);  // 2 * h; w stopniach geogr. !
    double a_2 = a * 2;  // 2 * h; w stopniach geogr. !


    double areaWidth = 5.0;  // w st. geo


    Set<Point> allVertices = new HashSet<>();
    List<List<Point>> graph = new ArrayList<>();

    //    Point[] vectors = new Point[12];
    Point[] vectors = new Point[4];     //for test
    Point[] areaBoundary = createArea();


//    public Point[] fillVectorsArray() {
//        vectors[0] = new Point(doubledH - 2 * latA, 0.0);
//        vectors[3] = new Point(0, 2 * (a + lngA));
//        vectors[6] = new Point(-latA, 0.0);
//        vectors[9] = new Point(0, -2 * a);
//
//        /* * * temporary values * * */
//        vectors[1] = new Point(doubledH - 2 * latA, 0.0);
//        vectors[2] = new Point(0, 2 * (a + lngA));
//        vectors[4] = new Point(-latA, 0.0);
//        vectors[5] = new Point(0, -2 * a);
//        vectors[7] = new Point(doubledH - 2 * latA, 0.0);
//        vectors[8] = new Point(0, 2 * (a + lngA));
//        vectors[10] = new Point(-latA, 0.0);
//        vectors[11] = new Point(0, -2 * a);
//
//        return vectors;
//    }

    // for test
    public Point[] fillVectorsArray() {
        vectors[0] = new Point(0.0, H);          // 0
        vectors[1] = new Point(a_2, 0.0);        // 3
        vectors[2] = new Point(0.0, -H);         // 6
        vectors[3] = new Point(-a_2, 0.0);       // 9

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
    public boolean areaContains(Point pointToCheck) {
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
        int vertexCounter = 1;
        Stack<Point> pointsToCheck = new Stack<>();
        Point currentPoint;
        Point currentNeighbour;
        double tmpX;
        double tmpY;
        vectors = fillVectorsArray();

        //init - put point A into pointsToCheck, graph and allVertices
        pointsToCheck.push(A);
        LinkedList<Point> arrA = new LinkedList<>();
        arrA.add(A);
        allVertices.add(A);
        graph.add(0, arrA);


        while (!pointsToCheck.isEmpty()) {

            // ściągamy pierwszy punkt ze stosu punktów do sprawdzenia
            currentPoint = pointsToCheck.pop();     //pop usuwa i zwraca (peek tylko zwraca) - ściąga ze stosu i nie musimy tego robić na końcu pętli
            tmpX = currentPoint.getX();
            tmpY = currentPoint.getY();


            //tworzymy sąsiadów wg wektorów "przesunięcia"
            for (int i = 0; i < vectors.length; i++) {

                //tworzymy danego sąsiada
                currentNeighbour =
                        new Point(tmpX + vectors[i].getX(),
                                tmpY + vectors[i].getY(), -1);

                //jeśli punkt nalezy do area
                if (areaContains(currentNeighbour)) {

                    if (!isPointInVertices(currentNeighbour)) {      // point nie ma jeszcze w zbiorze wierzchołków

                        //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
                        currentNeighbour.setNumber(vertexCounter);
                        vertexCounter++;
                        allVertices.add(currentNeighbour);

                        LinkedList<Point> list = new LinkedList<>(); //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
                        list.add(currentNeighbour);
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);
                        graph.add(currentNeighbour.getNumber(), list);
                        printGraph();

                        pointsToCheck.push(currentNeighbour); //dodajemy do listy pkt do spr

                    } else {                                //point jest już w zbiorze wierzchołków
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);
                    }
                }
            }


        }
    }

    /**
     * @param p1
     * @param p2
     */
    public void addVerticesToAdjacencyList(Point p1, Point p2) {
        int index = p1.getNumber();
        graph.get(index).add(p2);
//        graph.get(p2.getNumber()).add(p1);
        //czy wykonujemy symetrycznie? => czy graf jest skierowany??
        // tak - robimy symetrycznie - graf skierowany w obie strony, bo nie będziemy myśleć czy się cofamy czy nie

    }


    public void printGraph() {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + " --> ");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print("(" + graph.get(i).get(j).getX() + ", " + graph.get(i).get(j).getY() + ") ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isPointInVertices(Point pointToCheck) {
        for (Point p : allVertices) {
            if (p.getX() == pointToCheck.getX() && p.getY() == pointToCheck.getY()) {
                return true;
            }
        }
        return false;
    }


}
