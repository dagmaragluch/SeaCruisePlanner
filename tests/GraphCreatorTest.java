import org.junit.Assert;
import org.junit.Test;

public class GraphCreatorTest {
    Point A = new Point(53.934106, 14.274631);
    Point B = new Point(55.093889, 14.679056);
    GraphCreator graphCreator = new GraphCreator(A, B);

    @Test
    public void areaContainsTest() {
        Assert.assertTrue(graphCreator.areaContains(new Vertex(7, 19, -1)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(7, 12, -1)));
        //points on the boundary
        Assert.assertFalse(graphCreator.areaContains(new Vertex(13, 25, -1)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(5, 25, -1)));
    }


    @Test
    public void calculateEdgeAlphaTest() {
        Assert.assertEquals(30, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(2.0, 3.0, 0)));
        Assert.assertEquals(330, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(-2.0, 3.0, 0)));
        Assert.assertEquals(150, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(2.0, -3.0, 0)));
        Assert.assertEquals(210, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(-2.0, -3.0, 0)));
        Assert.assertEquals(0, graphCreator.calculateEdgeAlpha(new Vertex(2.0, 0.0, 0), new Vertex(2.0, 3.0, 0)));
        Assert.assertEquals(180, graphCreator.calculateEdgeAlpha(new Vertex(2.0, 0.0, 0), new Vertex(2.0, -3.0, 0)));
    }


}
