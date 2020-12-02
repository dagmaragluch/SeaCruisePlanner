import org.junit.Assert;
import org.junit.Test;

public class GraphCreatorTest {

    GraphCreator graphCreator = new GraphCreator();

    @Test
    public void areaContainsTest(){
        Assert.assertTrue(graphCreator.areaContains(new Point(7, 19)));
        Assert.assertFalse(graphCreator.areaContains(new Point(7, 12)));
        //points on the boundary
        Assert.assertFalse(graphCreator.areaContains(new Point(13, 25)));
        Assert.assertFalse(graphCreator.areaContains(new Point(5, 25)));

    }



}
