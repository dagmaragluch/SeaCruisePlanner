public class Edge {

    private Vertex start;
    private Vertex end;
    private Double weight;
    private int alpha;      //kąt między kierunkiem wiatru a osią jachtu
    private double edgeLength;  //długość odcinka reprezentowanego przez egde w rzeczywistości


    public Edge(Vertex start, Vertex end, int alpha, double edgeLength) {
        this.start = start;
        this.end = end;
        this.weight = 0.0;
        this.alpha = alpha;
        this.edgeLength = edgeLength;
    }


    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s -(%f)-> %s", start, weight, end);
    }
}
