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
        String[] ports = {"Gdynia", "Sztokholm", "Helsinki", "Gdańsk", "Świnoujście", "Kalmar", "Karlskrona", "Ronne", "Kłajpeda"};
        String[] dates = {"teraz", "jutro", "za 2 dni", "za 3 dni", "za 4 dni", "za 5 dni", "za 6 dni", "za 7 dni", "za 8 dni", "za 9 dni", "później"};
        String[] models = {"Delphia47", "Bavaria40_Cruiser", "Bavaria46_Cruiser", "Edel660", "Cookson50"};

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
        button.setText("Oblicz");
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
        tPath.setEditable(false);
        tTime = new JTextField();
        tTime.setPreferredSize(new Dimension(50, 20));
        tTime.setHorizontalAlignment(JTextField.CENTER);
        tTime.setEditable(false);
        tStraightDistance = new JTextField();
        tStraightDistance.setPreferredSize(new Dimension(65, 20));
        tStraightDistance.setHorizontalAlignment(JTextField.CENTER);
        tStraightDistance.setEditable(false);
        tRealDistance = new JTextField();
        tRealDistance.setPreferredSize(new Dimension(65, 20));
        tRealDistance.setHorizontalAlignment(JTextField.CENTER);
        tRealDistance.setEditable(false);
        tVelocity = new JTextField();
        tVelocity.setPreferredSize(new Dimension(50, 20));
        tVelocity.setHorizontalAlignment(JTextField.CENTER);
        tVelocity.setEditable(false);

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


        leftPanel.add(lStart, BorderLayout.WEST);
        leftPanel.add(cbStart, BorderLayout.WEST);
        leftPanel.add(lEnd, BorderLayout.WEST);
        leftPanel.add(cbEnd, BorderLayout.WEST);
        leftPanel.add(lDate, BorderLayout.WEST);
        leftPanel.add(cbDate, BorderLayout.WEST);
        leftPanel.add(lYacht, BorderLayout.WEST);
        leftPanel.add(cbYacht, BorderLayout.WEST);
        leftPanel.add(button, BorderLayout.CENTER);
//        leftPanel.add(lCommunicat, BorderLayout.CENTER);

        rightPanel.add(lPath, BorderLayout.WEST);
        rightPanel.add(tPath, BorderLayout.WEST);
        rightPanel.add(lTime, BorderLayout.WEST);
        rightPanel.add(tTime, BorderLayout.WEST);
        rightPanel.add(lStraightDistance, BorderLayout.WEST);
        rightPanel.add(tStraightDistance, BorderLayout.WEST);
        rightPanel.add(lRealDistance, BorderLayout.WEST);
        rightPanel.add(tRealDistance, BorderLayout.WEST);
        rightPanel.add(lVelocity, BorderLayout.WEST);
        rightPanel.add(tVelocity, BorderLayout.WEST);
        rightPanel.add(lCoordinates, BorderLayout.CENTER);
        rightPanel.add(sCoordinates, BorderLayout.CENTER);

        bigContainer.setLayout(new GridLayout(1, 2));
        bigContainer.add(leftPanel);
        bigContainer.add(rightPanel);


        frame.add(bigContainer);
        frame.setSize(542, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
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
                    try {
                        support = new Support(start, end, yacht, date);
                        support.prepareGraph();
                    } catch (Exception ex) {
                        lastA = "";
                        lastB = "";
                        JOptionPane.showMessageDialog(frame, "Brak połączenia z Internetem albo wykorzystano dzienny limit zapytań do API", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
}
