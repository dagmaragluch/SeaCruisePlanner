import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MyGui extends JFrame implements ActionListener {

    Support support;

    static JFrame frame;
    static JLabel lStart, lEnd, lDate, lYacht, lCommunicat;
    static JComboBox<String> cbStart, cbEnd, cbDate, cbYacht;
    static JButton button;

    static JLabel lPath, lTime, lStraightDistance, lRealDistance, lVelocity, lCoordinates;
    static JTextField tPath, tTime, tStraightDistance, tRealDistance, tVelocity;
    static JScrollPane sCoordinates;
    static JTextArea display;

    String lastA = "";
    String lastB = "";


    public static void main(String[] args) {
        setGuiElements();
    }


    public static void setGuiElements() {
        frame = new JFrame("SeaCruisePlanner");
        MyGui myGui = new MyGui();
        frame.setLayout(new BorderLayout());
        String[] ports = {"Gdynia", "Sztokholm", "Helsinki", "Gdańsk", "Świnoujście", "Kalmar", "Karlskrona", "Ronne", "Kłajpeda", "Visby"};
        String[] dates = {"teraz", "jutro", "za 2 dni", "za 3 dni", "za 4 dni", "za 5 dni", "za 6 dni", "za 7 dni", "za 8 dni", "za 9 dni", "później"};
        String[] models = {"Bavaria40_Cruiser", "Delphia47", "Bavaria46_Cruiser", "Edel660", "Cookson50"};

        cbStart = new JComboBox<>(ports);
        cbStart.addActionListener(myGui);
        cbStart.setSelectedIndex(0);
        lStart = new JLabel("Port startowy");

        cbEnd = new JComboBox<>(ports);
        cbEnd.addActionListener(myGui);
        cbEnd.setSelectedIndex(1);
        lEnd = new JLabel("Port końcowy");

        cbYacht = new JComboBox<>(models);
        cbYacht.addActionListener(myGui);
        cbYacht.setSelectedIndex(0);
        lYacht = new JLabel("Model jachtu");

        cbDate = new JComboBox<>(dates);
        cbDate.addActionListener(myGui);
        cbDate.setSelectedIndex(0);
        lDate = new JLabel("Data wypłynięcia");

        button = new JButton();
        button.setText("Wypłyń!");  // na głębię
        button.addActionListener(myGui);
//        button.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        lPath = new JLabel("trasa:");
        lTime = new JLabel("czas:");
        lStraightDistance = new JLabel("odległość w linii prostej:");
        lRealDistance = new JLabel("długość wyzanczonej trasy:");
        lVelocity = new JLabel("średnia prędkość na trasie:");
        lCoordinates = new JLabel("Wyznaczona trasa:");
//        lCommunicat = new JLabel();
//        lCommunicat.setText("Trwa konstruowanie grafu i obliczanie trasy. \n Może to trochę zająć...");
//        lCommunicat.setVisible(true);

        tPath = new JTextField();
        tPath.setPreferredSize(new Dimension(150, 20));
        tPath.setHorizontalAlignment(JTextField.CENTER);
        tTime = new JTextField();
        tTime.setPreferredSize(new Dimension(50, 20));
        tTime.setHorizontalAlignment(JTextField.CENTER);
        tStraightDistance = new JTextField();
        tStraightDistance.setPreferredSize(new Dimension(50, 20));
        tStraightDistance.setHorizontalAlignment(JTextField.CENTER);
        tRealDistance = new JTextField();
        tRealDistance.setPreferredSize(new Dimension(50, 20));
        tRealDistance.setHorizontalAlignment(JTextField.CENTER);
        tVelocity = new JTextField();
        tVelocity.setPreferredSize(new Dimension(50, 20));
        tVelocity.setHorizontalAlignment(JTextField.CENTER);

        display = new JTextArea(15, 20);
        display.setEditable(false);
        sCoordinates = new JScrollPane(display);
        sCoordinates.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        JPanel bigContainer = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

//        leftPanel.setLayout(new GridLayout(5, 5, 10, 10));


        leftPanel.add(lStart);
        leftPanel.add(cbStart);
        leftPanel.add(lEnd);
        leftPanel.add(cbEnd);
        leftPanel.add(lDate);
        leftPanel.add(cbDate);
        leftPanel.add(lYacht);
        leftPanel.add(cbYacht);
        leftPanel.add(button, BorderLayout.CENTER);
//        leftPanel.add(lCommunicat, BorderLayout.CENTER);

        rightPanel.add(lPath, BorderLayout.WEST);
        rightPanel.add(tPath, BorderLayout.EAST);
        rightPanel.add(lTime);
        rightPanel.add(tTime);
        rightPanel.add(lStraightDistance);
        rightPanel.add(tStraightDistance);
        rightPanel.add(lRealDistance);
        rightPanel.add(tRealDistance);
        rightPanel.add(lVelocity);
        rightPanel.add(tVelocity);
        rightPanel.add(lCoordinates);
        rightPanel.add(sCoordinates);

        bigContainer.setLayout(new GridLayout(1, 2));
        bigContainer.add(leftPanel);
        bigContainer.add(rightPanel);


        frame.add(bigContainer);
        frame.setSize(527, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            String start = Objects.requireNonNull(cbStart.getSelectedItem()).toString();
            String end = Objects.requireNonNull(cbEnd.getSelectedItem()).toString();
            String yacht = Objects.requireNonNull(cbYacht.getSelectedItem()).toString();
            String date = String.valueOf(cbDate.getSelectedIndex());

            if (start.equals(end)) {
                JOptionPane.showMessageDialog(frame, "Wybierz inny port!");
            } else {
                if (!lastA.equals(start) || !lastB.equals(end)) {   // graph NOT exist
                    tPath.setText(start + " - " + end);
                    support = new Support(getInputData(start, end, yacht, date));
                    support.prepareGraph();
                    lastA = start;
                    lastB = end;
                } else {
                    support.updateDijkstra(yacht, date);    //these sames ports - graph already exist
                }
                Output output = support.runDijkstra();

                if (output.isGood()) {
                    tTime.setText(output.getTime());
                    tVelocity.setText(output.getAvgVelocity());
                    tStraightDistance.setText(output.getStraightDistance());
                    tRealDistance.setText(output.getCalculatedDistance());
                    display.setText(output.getPath());
                } else {
                    JOptionPane.showMessageDialog(frame, "Warunki pogdowe na podanej trasie, w podanym terminie są niesprzyjające.");
                }


            }


        }


    }

    public String[] getInputData(String start, String end, String yacht, String date) {
        String[] inputData = new String[4];
        inputData[0] = start;
        inputData[1] = end;
        inputData[2] = yacht;
        inputData[3] = date;

        System.err.println("start =  " + start);
        System.err.println("end =  " + end);
        System.err.println("yacht =  " + yacht);
        System.err.println("date =  " + date);

        return inputData;
    }


}
