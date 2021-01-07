import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PolarPlotReader {

    public static Map<Double, Double[]> readCSVFile(String csvFile) {

        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        Map<Double, Double[]> bigMap = new HashMap<>();


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                String[] lineAsArray = line.split(cvsSplitBy);
                if (lineAsArray.length != 8) {
                    System.err.println("Incorrect number of data in line:\n" + line);
                    break;
                }
                Double[] array = new Double[7];
                Double windSpeed = Double.parseDouble(lineAsArray[0]);  //wind speed = key

                for (int i = 1; i < lineAsArray.length; i++) {
                    try {
                        array[i - 1] = Double.parseDouble(lineAsArray[i]);
                    } catch (NumberFormatException ex) {
                        System.err.println("invalid number format in line:\n" + line);
                    }
                }
                bigMap.put(windSpeed, array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bigMap;
    }


}
