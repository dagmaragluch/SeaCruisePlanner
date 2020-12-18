import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DijkstraTest {

    GraphCreator graphCreator = new GraphCreator();
    private static Dijkstra dijkstra;
    private static Vertex start;
    Graph graph;


    @Before
    public void before() {
        graphCreator.createGraph();
        start = graphCreator.A;
        graph = graphCreator.GRAPH;
        dijkstra = new Dijkstra(graph);
    }


    @Test
    public void quantizeWindSpeedTest() {
        Assert.assertEquals(3.0, dijkstra.quantizeWindSpeed(dijkstra.KNOTS_TO_METERS_PER_SECOND * 2.3), 0.0);
        Assert.assertEquals(6.0, dijkstra.quantizeWindSpeed(dijkstra.KNOTS_TO_METERS_PER_SECOND * 4.6), 0.0);
        Assert.assertEquals(12.0, dijkstra.quantizeWindSpeed(dijkstra.KNOTS_TO_METERS_PER_SECOND * 13.2), 0.0);
        Assert.assertEquals(20.0, dijkstra.quantizeWindSpeed(dijkstra.KNOTS_TO_METERS_PER_SECOND * 19.7), 0.0);
        Assert.assertEquals(20.0, dijkstra.quantizeWindSpeed(dijkstra.KNOTS_TO_METERS_PER_SECOND * 25.0), 0.0);
    }


    @Test
    public void calculateAngleTest() {
        Assert.assertEquals(0, dijkstra.calculateAngle(90, 90));
        Assert.assertEquals(180, dijkstra.calculateAngle(90, 270));
        Assert.assertEquals(180, dijkstra.calculateAngle(270, 90));
        Assert.assertEquals(270, dijkstra.calculateAngle(0, 270));
        Assert.assertEquals(30, dijkstra.calculateAngle(90, 120));
        Assert.assertEquals(330, dijkstra.calculateAngle(180, 150));
        Assert.assertEquals(30, dijkstra.calculateAngle(180, 210));
    }


    @Test
    public void windDirectionToIndexTest() {
        Assert.assertEquals(0, dijkstra.windDirectionToIndex(90, 90));
        Assert.assertEquals(1, dijkstra.windDirectionToIndex(90, 120));
        Assert.assertEquals(6, dijkstra.windDirectionToIndex(90, 270));
        Assert.assertEquals(1, dijkstra.windDirectionToIndex(180, 150));
        Assert.assertEquals(3, dijkstra.windDirectionToIndex(0, 270));
    }

}
