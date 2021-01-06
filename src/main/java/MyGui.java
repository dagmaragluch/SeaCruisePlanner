import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MyGui extends JFrame implements ActionListener {

    static JFrame frame;
    static JLabel lStart, lEnd, lDate, lYacht;
    static JComboBox<String> cbStart, cbEnd, cbDate, cbYacht;
    static JButton button;

    static JLabel lPath, lTime, lLength1, lLength2, lVelocity, lCoordinates;
    static JTextField tPath, tTime, tLength1, tLength2, tVelocity;
    static JScrollPane sCoordinates;
    static JTextArea display;


    public static void main(String[] args) {
        setGuiElements();
    }


    public static void setGuiElements() {
        frame = new JFrame("SeaCruisePlanner");
        MyGui myGui = new MyGui();
        frame.setLayout(new BorderLayout());
        String[] ports = {"Helsinki", "Gdynia", "Gdańsk", "Sztokholm"};
        String[] dates = {"teraz", "jutro", "za 2 dni", "za 3 dni"};
        String[] models = {"Bavaria40 Cruiser", "Delphia47", "Bavaria46 Cruiser", "Edel660", "Cookson50"};

        cbStart = new JComboBox<>(ports);
        cbStart.addActionListener(myGui);
        cbStart.setSelectedIndex(0);
        lStart = new JLabel("Wybierz port startowy");

        cbEnd = new JComboBox<>(ports);
        cbEnd.addActionListener(myGui);
        cbEnd.setSelectedIndex(0);
        lEnd = new JLabel("Wybierz port końcowy");

        cbYacht = new JComboBox<>(models);
        cbYacht.addActionListener(myGui);
        cbYacht.setSelectedIndex(0);
        lYacht = new JLabel("Wybierz model jachtu");

        cbDate = new JComboBox<>(dates);
        cbDate.addActionListener(myGui);
        cbDate.setSelectedIndex(0);
        lDate = new JLabel("Wybierz kiedy planujesz wypłynąć");

        button = new JButton();
        button.setText("Wypłyń!");  // na głębię
        button.addActionListener(myGui);

        lPath = new JLabel("trasa:");
        lTime = new JLabel("czas:");
        lLength1 = new JLabel("odległość w linii prostej:");
        lLength2 = new JLabel("długość wyzanczonej trasy:");
        lVelocity = new JLabel("średnia prędkość na trasie:");
        lCoordinates = new JLabel("Wyznaczona trasa:");

        tPath = new JTextField();
        tPath.setPreferredSize(new Dimension(150, 20));
        tTime = new JTextField();
        tTime.setPreferredSize(new Dimension(50, 20));
        tLength1 = new JTextField();
        tLength1.setPreferredSize(new Dimension(50, 20));
        tLength2 = new JTextField();
        tLength2.setPreferredSize(new Dimension(50, 20));
        tVelocity = new JTextField();
        tVelocity.setPreferredSize(new Dimension(50, 20));

        display = new JTextArea(15, 30);
        display.setEditable(false);
        sCoordinates = new JScrollPane(display);
        sCoordinates.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        JPanel bigContainer = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

//        leftPanel.setLayout(new GridLayout(5, 5));


        leftPanel.add(lStart);
        leftPanel.add(cbStart);
        leftPanel.add(lEnd);
        leftPanel.add(cbEnd);
        leftPanel.add(lDate);
        leftPanel.add(cbDate);
        leftPanel.add(lYacht);
        leftPanel.add(cbYacht);
        leftPanel.add(button);

        rightPanel.add(lPath);
        rightPanel.add(tPath);
        rightPanel.add(lTime);
        rightPanel.add(tTime);
        rightPanel.add(lLength1);
        rightPanel.add(tLength1);
        rightPanel.add(lLength2);
        rightPanel.add(tLength2);
        rightPanel.add(lVelocity);
        rightPanel.add(tVelocity);
        rightPanel.add(lCoordinates);
        rightPanel.add(sCoordinates);

        bigContainer.setLayout(new GridLayout(1, 2));
        bigContainer.add(leftPanel);
        bigContainer.add(rightPanel);


        frame.add(bigContainer);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {

            String start = Objects.requireNonNull(cbStart.getSelectedItem()).toString();
            String end = Objects.requireNonNull(cbEnd.getSelectedItem()).toString();
            String date = String.valueOf(cbDate.getSelectedIndex());
            String yacht = Objects.requireNonNull(cbYacht.getSelectedItem()).toString();

            if (start.equals(end)) {
                JOptionPane.showMessageDialog(frame, "Wybierz inny port!");
            } else {
                getInputData(start, end, date, yacht);
                tPath.setText(start + " - " + end);
                tTime.setText(String.valueOf(start.length()));
            }

        }


    }

    public String[] getInputData(String start, String end, String date, String yacht) {
        String[] inputData = new String[4];
        inputData[0] = start;
        inputData[1] = end;
        inputData[2] = date;
        inputData[3] = yacht;

        System.err.println("start =  " + start);
        System.err.println("end =  " + end);
        System.err.println("date =  " + date);
        System.err.println("yacht =  " + yacht);

        return inputData;
    }


}
