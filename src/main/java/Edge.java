public class Edge {

    private Vertex start;
    private Vertex end;
    private Double weight;
    private int alpha;      //kąt między kierunkiem wiatru a osią jachtu
    private double edgeLength;  //długość odcinka reprezentowanego przez egde w rzeczywistości - w milach morskich !


    public Edge(Vertex start, Vertex end, int alpha) {
        this.start = start;
        this.end = end;
        this.weight = 0.0;
        this.alpha = alpha;
        this.edgeLength = distance(start, end, "N");
    }


    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getAlpha() {
        return alpha;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    @Override
    public String toString() {
        if (weight < 999999999999999999.9)
            return String.format("%s -(%.2f)-> %s", start.getIndex(), weight, end.getIndex());
        else
            return String.format("%s -(infinity)-> %s", start.getIndex(), end.getIndex());
    }

    /**
     * code from: https://www.geodatasource.com/developers/java
     * <p>
     * lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)
     * lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)
     * unit = the unit you desire for results
     * where: 'M' is statute miles (default)
     * 'K' is kilometers
     * 'N' is nautical miles
     */

    public static double distance(Vertex p1, Vertex p2, String unit) {

        double lat1 = p1.getX();
        double lng1 = p1.getY();
        double lat2 = p2.getX();
        double lng2 = p2.getY();

        double theta = lng1 - lng2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

}
