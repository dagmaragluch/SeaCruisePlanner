import java.util.HashMap;
import java.util.Map;

public class Delphia47 {

    Map<Double, Double[]> delphia47 = fillData();

    public Map<Double, Double[]> fillData() {

        // dane w węzłach !!!

        Map<Double, Double[]> delphia47 = new HashMap<>();

        //   degrees:      0   30   60   90  120  150  180
        Double[] arr0 = {0.0, 1.5, 2.8, 3.4, 3.0, 1.8, 1.6};
        Double[] arr1 = {0.0, 3.0, 5.0, 5.9, 5.3, 3.8, 3.1};
        Double[] arr2 = {0.0, 4.2, 6.1, 6.6, 6.5, 5.4, 4.5};
        Double[] arr3 = {0.0, 4.5, 6.6, 7.5, 7.2, 6.5, 5.7};
        Double[] arr4 = {0.0, 5.0, 7.2, 8.1, 8.0, 7.1, 6.55};
        Double[] arr5 = {1.0, 5.0, 7.8, 8.7, 9.0, 8.2, 7.7};

        delphia47.put(3.0, arr0);
        delphia47.put(6.0, arr1);
        delphia47.put(9.0, arr2);
        delphia47.put(12.0, arr3);
        delphia47.put(15.0, arr4);
        delphia47.put(20.0, arr5);

        return delphia47;
    }
}
