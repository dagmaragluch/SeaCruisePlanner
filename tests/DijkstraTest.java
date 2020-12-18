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
    public void quantizeWindSpeedTest(){
        Assert.assertEquals(3.0, dijkstra.quantizeWindSpeed(2.3), 0.0);
        Assert.assertEquals(6.0, dijkstra.quantizeWindSpeed(4.6), 0.0);
        Assert.assertEquals(12.0, dijkstra.quantizeWindSpeed(13.2), 0.0);
        Assert.assertEquals(20.0, dijkstra.quantizeWindSpeed(19.7), 0.0);
        Assert.assertEquals(20.0, dijkstra.quantizeWindSpeed(25.0), 0.0);
    }

}
