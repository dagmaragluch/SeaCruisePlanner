import org.junit.Assert;
import org.junit.Test;

public class GraphCreatorTest {

    GraphCreator graphCreator = new GraphCreator();

    @Test
    public void areaContainsTest(){
        Assert.assertTrue(graphCreator.areaContains(new Vertex(7, 19, -1)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(7, 12, -1)));
        //points on the boundary
        Assert.assertFalse(graphCreator.areaContains(new Vertex(13, 25, -1)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(5, 25, -1)));

    }



}
