package bsu.rfe.course2.group9.Kachuk;


//Добавить в модель таблицы 3-й столбец «Значение простое?»,
//содержащий bool зн-я и принимающий значение
// true - целая часть значения многочлена - простое число
// false – в противном случае.
//Обеспечить отображение значений 3-го  столбца как флажков.

//Изменить визуализатор: ячейки, в дробной части которых стоят числа 1, 3, 5
//окрашивались в специальный цвет

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem getInfoAboutAuthor;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;

    private Box hBoxResult;

    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;

    public MainFrame(Double[] coefficients) {
        super("Polynomial tabulation according to Horner's scheme");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Table");
        menuBar.add(tableMenu);
        JMenu AboutMenu = new JMenu("About the program");
        menuBar.add(AboutMenu);

        Action saveToTextAction = new AbstractAction("Save to txt file") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        Action saveToGraphicsAction = new AbstractAction("Save data for plotting") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION);
                saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        Action searchValueAction = new AbstractAction("Find polynomial value") {
            public void actionPerformed(ActionEvent event) {
                String value =
                        JOptionPane.showInputDialog(MainFrame.this, "Enter the value for search",
                                "Search value", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);

        Action GetInfo = new AbstractAction("About author") {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,"Kachuk Irina, group 9", "Author",
                        JOptionPane.QUESTION_MESSAGE);
            }
        };
        getInfoAboutAuthor = AboutMenu.add(GetInfo);
        getInfoAboutAuthor.setEnabled(true);

        JLabel labelForFrom = new JLabel("X changes from:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("to:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("by step:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());


        hboxRange.setPreferredSize(new Dimension(
                Double.valueOf(hboxRange.getMaximumSize().getWidth()).intValue(),
                Double.valueOf(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hboxRange, BorderLayout.NORTH);
        // вот тут надо менят на SO/utH
        JButton buttonCalc = new JButton("Calculate");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Floating-point error!", "Numbers format error!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton buttonReset = new JButton("Clear");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension(Double.valueOf(hboxButtons.getMaximumSize().getWidth()).intValue(),
                Double.valueOf(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        // тут тоже надо менять
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
                out.writeDouble((Double) data.getValueAt(i, 2));
            }
            out.close();
        } catch (Exception e) {
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);

            out.println("The result of polynomial tabulation");
            out.print("Polynomial: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));

                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Interval from " + data.getFrom() + " to " +
                    data.getTo() + " by step " + data.getStep());
            out.println("====================================================");
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Value in the spot " + data.getValueAt(i, 0)
                        + " = " + data.getValueAt(i, 1) +
                        " is primal: " + data.getValueAt(i, 2));
            }

            out.close();
        } catch (FileNotFoundException e) {
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try to add coefficients in the args[]!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error in string converting '" +
                    args[i] + "' to Double");
            System.exit(-2);
        }

        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}