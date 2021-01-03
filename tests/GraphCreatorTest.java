import org.junit.Assert;
import org.junit.Test;

public class GraphCreatorTest {
    Point A = new Point(54.421303, 18.693822);   // Gdańsk
    Point B = new Point(57.633915, 18.269643);   // Visby
    GraphCreator graphCreator = new GraphCreator(A, B);


    @Test
    public void calculateEdgeAlphaTest() {
        Assert.assertEquals(30, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(2.0, 3.0, 0)));
        Assert.assertEquals(330, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(-2.0, 3.0, 0)));
        Assert.assertEquals(150, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(2.0, -3.0, 0)));
        Assert.assertEquals(210, graphCreator.calculateEdgeAlpha(new Vertex(0.0, 0.0, 0), new Vertex(-2.0, -3.0, 0)));
        Assert.assertEquals(0, graphCreator.calculateEdgeAlpha(new Vertex(2.0, 0.0, 0), new Vertex(2.0, 3.0, 0)));
        Assert.assertEquals(180, graphCreator.calculateEdgeAlpha(new Vertex(2.0, 0.0, 0), new Vertex(2.0, -3.0, 0)));
    }


    @Test
    public void areaContainsTest() {
        Point A = new Point(59.48040, 19.16869);   // Sztokholm
        Point B = new Point(54.51683, 18.55586);   // Gdynia
        GraphCreator graphCreator = new GraphCreator(A, B);

        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.73990, 19.16869, 0)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.7399, 19.31869, 1)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.61015, 19.39316, 2)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.4804, 19.4686, 3)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.35065, 19.39316, 4)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.2209, 19.31869, 5)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.2209, 19.16869, 6)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.2209, 19.01869, 7)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.35065, 18.94422, 8)));
        Assert.assertTrue(graphCreator.areaContains(new Vertex(59.4804, 18.86869, 9)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.61015, 18.94422, 10)));
        Assert.assertFalse(graphCreator.areaContains(new Vertex(59.7399, 19.01869, 11)));
    }


    @Test
    public void calculateDistanceTest() {
        Vertex v1 = new Vertex(59.480396, 19.168691, 0);
        Vertex v2 = new Vertex(54.51683, 18.55586, 0);
        Vertex v3 = new Vertex(59.48040, 19.46869, 0);
        Vertex v4 = new Vertex(59.73990, 18.86869, 0);

        Assert.assertEquals(0.0, Edge.distance(v1, v1, "K"), 0.000000001);
        Assert.assertEquals(Edge.distance(v2, v1, "K"), Edge.distance(v1, v2, "K"), 0.1);
        Assert.assertEquals(16.929054, Edge.distance(v1, v3, "K"), 0.1);
        Assert.assertEquals(33.405278, Edge.distance(v1, v4, "K"), 0.1);
        Assert.assertEquals(18.037, Edge.distance(v1, v4, "N"), 0.1);
    }

    @Test
    public void calculateArea() {
        double x_a = 2.0;
        double y_a = 2.0;
        double x_b = 6.0;
        double y_b = 4.0;
        double d = 1.0;

        double a = (y_a - y_b) / (x_a - x_b);
        double b_prim_dla_A = y_a + x_a / a;

        double mianownik = a * a + 1;
        double polowa_licznika_dla_A = x_a * a * a + x_a;
        double pierwiastek = Math.sqrt(d * d * a * a * a * a + d * d * a * a);

        double x1 = (polowa_licznika_dla_A - pierwiastek) / mianownik;
        double x2 = (polowa_licznika_dla_A + pierwiastek) / mianownik;

        double y1 = b_prim_dla_A - x1 / a;
        double y2 = b_prim_dla_A - x2 / a;

        double polowa_licznika_dla_B = x_b * a * a + x_b;
        double b_prim_dla_B = y_b + x_b / a;

        double x1_b = (polowa_licznika_dla_B - pierwiastek) / mianownik;
        double x2_b = (polowa_licznika_dla_B + pierwiastek) / mianownik;
        double y1_b = b_prim_dla_B - x1_b / a;
        double y2_b = b_prim_dla_B - x2_b / a;

        System.out.println("P = " + x1 + ", " + y1);
        System.out.println("Q = " + x2 + ", " + y2);
        System.out.println("R = " + x1_b + ", " + y1_b);
        System.out.println("S = " + x2_b + ", " + y2_b);
    }

}
