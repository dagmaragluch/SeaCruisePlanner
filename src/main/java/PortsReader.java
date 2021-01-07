import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortsReader {

    public static Map<String, Point> readPorts(String portsFile) {

        BufferedReader br = null;
        String line;
        String separator = ",";

        Map<String, Point> portsMap = new HashMap<>();


        try {
            br = new BufferedReader(new FileReader(portsFile));
            while ((line = br.readLine()) != null) {

                String[] lineAsArray = line.split(separator);
                String portName = lineAsArray[0];
                Point location = new Point(Double.parseDouble(lineAsArray[1]), Double.parseDouble(lineAsArray[2]));

                portsMap.put(portName, location);

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
        return portsMap;
    }

}
