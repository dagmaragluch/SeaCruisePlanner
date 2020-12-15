public class Edge {

    Vertex start;
    Vertex end;
    Double weight;
    int alpha;      //kąt między kierunkiem wiatru a osią jachtu
    double edgeLength;  //długość odcinka reprezentowanego przez egde w rzeczywistości

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
