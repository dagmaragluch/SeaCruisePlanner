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

//    double a = 0.2;     // dł boku trójkąta równobocznego; w stopniach geogr. !
//    double h = a / 2 * Math.sqrt(3);  // wysokość w trójkącie równobocznym; też w stopniach geogr. !
//    double doubledH = a * Math.sqrt(3);  // 2 * h; w stopniach geogr. !

    double a = 1.0;
    double doubledH = 2.0;


    double areaWidth = 5.0;  // w st. geo


    Set<Point> allVertices = new HashSet<>();
    List<List<Point>> graph = new ArrayList<>();

    //    Point[] vectors = new Point[12];
    Point[] vectors = new Point[4];     //for test
    int vertexCounter = 0;
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


    public Point[] fillVectorsArray() {
        vectors[0] = new Point(doubledH - 2 * latA, 0.0);
        vectors[1] = new Point(0, 2 * (a + lngA));
        vectors[2] = new Point(-latA, 0.0);
        vectors[3] = new Point(0, -2 * a);

        return vectors;
    }


    public Point[] createArea() {
        Point[] areaBoundary = new Point[4];
        areaBoundary[0] = new Point(-areaWidth, lngA);
        areaBoundary[1] = new Point(areaWidth, lngA);
        areaBoundary[2] = new Point(areaWidth, lngB);
        areaBoundary[3] = new Point(-areaWidth, lngB);
        return areaBoundary;
    }

    /**
     * Return true if the given point is contained inside the boundary.
     *
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

    public void createGraph() {     //change name
        int l = 1;  //counter
        Stack<Point> pointsToCheck = new Stack<>();
        Point currentPoint;
        Point currentNeighbour;
        double tmpX;
        double tmpY;
        vectors = fillVectorsArray();

        //init - put point A into pointsToCheck, graph and allVertices
        pointsToCheck.push(A);
        LinkedList<Point> arrA = new LinkedList<>();
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
                if (areaContains(currentNeighbour)) {    //  SPRAWDZIĆ CZY SPRAWDZA WARTOŚĆ CZY ADRES!!!
                    if (!allVertices.contains(currentNeighbour)) {      // point nie ma jeszcze w zbiorze wierzchołków

                        //nadajemy numer i dodajemy do zbioru wszystkich wierzchołków
                        currentNeighbour.setNumber(l);
                        l++;
                        allVertices.add(currentNeighbour);

                        LinkedList<Point> list = new LinkedList<>(); //tworzymy nowa listę, dodajemy wierzchołek i dodajemy listę do grafu
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);
                        graph.add(currentNeighbour.getNumber(), list);

                        pointsToCheck.push(currentNeighbour); //dodajemy do listy pkt do spr

                    } else {                                //point jest już w zbiorze wierzchołków
                        addVerticesToAdjacencyList(currentPoint, currentNeighbour);
//                        addVerticesToAdjacencyList(currentNeighbour, currentPoint);   //czy wykonujemy symetrycznie? => czy graf jest skierowany??
                        // tak - robimy symetrycznie - graf skierowany w obie strony, bo nie będziemy myśleć czy się cofamy czy nie

                    }
                }
            }


        }


    }

    /**
     * @param startVertex
     * @param neighbour
     */
    public void addVerticesToAdjacencyList(Point startVertex, Point neighbour) {
        int index = startVertex.getNumber();
        graph.get(index).add(neighbour);

    }


}
