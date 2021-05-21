package ui;

import appdata.AppData;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import com.formdev.flatlaf.FlatIntelliJLaf;
import entities.Airline;
import entities.Flight;

public class GUI extends JFrame {
    private final AppData appdata;
    private Airline selectedAirline = null;
    private Flight selectedFlight = null;

    private JPanel rootPanel;
    private JTabbedPane tabbedPane;
    private JPanel airlineTab;
    private JPanel flightTab;
    private JPanel createTab;
    private JTable airlineTable;
    private JScrollPane airlineTableWrapper;
    private JTable flightTable;
    private JScrollPane flightTableWrapper;
    private JTextField airlineNameTextField;
    private JTextField airlineCountryTextFiled;
    private JLabel airlineIdLabel;
    private JLabel airlineNameLabel;
    private JLabel airlineCountryLabel;
    private JLabel airlineIdValueLabel;
    private JButton airlineUpdateBtn;
    private JButton airlineRemoveBtn;
    private JComboBox flightAirlineComboBox;
    private JTextField flightDepartureTextField;
    private JTextField flightArrivalTextField;
    private JSpinner flightProceSpinner;
    private JFormattedTextField flightDepartureTimeFormattedTextField;
    private JLabel flightIdLabel;
    private JLabel flightIdValueLabel;
    private JLabel flightAirlineIdLabel;
    private JLabel flightDepartureLabel;
    private JLabel flightArrivalLabel;
    private JLabel flightPriceLabel;
    private JLabel flightDepartureTimeLabel;
    private JButton flightUpdateBtn;
    private JButton flightRemoveBtn;

    public GUI() {
        $$$setupUI$$$();
        ResourceBundle resource = ResourceBundle.getBundle("xml");
        appdata = AppData.loadFromFile(resource.getString("filename.airlines"), resource.getString("filename.flights"));

        setContentPane(rootPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 1000));

        initTable(airlineTable, new String[]{"id", "name", "country"});
        updateAirlinesTable(airlineTable);

        initTable(flightTable, new String[]{"id", "airline", "from", "to", "price", "departure time"});
        updateFlightTable(flightTable);

        airlineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = airlineTable.rowAtPoint(e.getPoint());
                if (row != -1) {
                    var model = airlineTable.getModel();

                    int id = (int) model.getValueAt(row, 0);
                    var tmp = appdata.getAirlineData().get(id);
                    selectedAirline = new Airline(id, tmp.getName(), tmp.getCountry());

                    airlineIdValueLabel.setText(String.valueOf(id));
                    airlineNameTextField.setText(selectedAirline.getName());
                    airlineCountryTextFiled.setText(selectedAirline.getCountry());
                } else {
                    clearSelectedAirline();
                }
            }
        });

        airlineUpdateBtn.addActionListener(e -> {
            if (selectedAirline == null) {
                return;
            }

            selectedAirline.setName(airlineNameTextField.getText());
            selectedAirline.setCountry(airlineCountryTextFiled.getText());

            appdata.getAirlineData().update(selectedAirline);

            updateAirlinesTable(airlineTable);
        });

        airlineRemoveBtn.addActionListener(e -> {
            if (selectedAirline == null) {
                return;
            }

            appdata.getAirlineData().remove(selectedAirline.getId());

            clearSelectedAirline();
            updateAirlinesTable(airlineTable);
        });
    }

    public void initTable(JTable table, String[] columnNames) {
        var model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }

        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(model);
    }

    public void updateAirlinesTable(JTable table) {
        var model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);

        var list = appdata.getAirlineData().getAll();
        for (var item : list) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getCountry()
            });
        }
    }

    public void updateFlightTable(JTable table) {
        var model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);

        var list = appdata.getFlightData().getAll();
        for (var item : list) {
            model.addRow(new Object[]{
                    item.getId(),
                    appdata.getAirlineData().get(item.getAirlineId()).getName(),
                    item.getDepartureAirport(),
                    item.getAirlineId(),
                    item.getPrice(),
                    item.getDepartureDateTime().toString()
            });
        }
    }

    private void clearSelectedAirline() {
        selectedAirline = null;
        airlineIdValueLabel.setText("");
        airlineNameTextField.setText("");
        airlineCountryTextFiled.setText("");
    }

    public static void main(String[] args) {
        FlatIntelliJLaf.install();
        new GUI();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(15, 15, 15, 15), -1, -1));
        Font rootPanelFont = this.$$$getFont$$$("JetBrains Mono Medium", -1, -1, rootPanel.getFont());
        if (rootPanelFont != null) rootPanel.setFont(rootPanelFont);
        tabbedPane = new JTabbedPane();
        rootPanel.add(tabbedPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        airlineTab = new JPanel();
        airlineTab.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Airlines Data", airlineTab);
        airlineTableWrapper = new JScrollPane();
        airlineTab.add(airlineTableWrapper, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        airlineTableWrapper.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        airlineTable = new JTable();
        airlineTable.setFillsViewportHeight(true);
        airlineTable.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
        airlineTableWrapper.setViewportView(airlineTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        airlineTab.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineIdLabel = new JLabel();
        airlineIdLabel.setHorizontalAlignment(4);
        airlineIdLabel.setHorizontalTextPosition(4);
        airlineIdLabel.setText("id:");
        panel1.add(airlineIdLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        airlineNameLabel = new JLabel();
        airlineNameLabel.setHorizontalAlignment(0);
        airlineNameLabel.setHorizontalTextPosition(0);
        airlineNameLabel.setText("name:");
        panel1.add(airlineNameLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineNameTextField = new JTextField();
        panel1.add(airlineNameTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        airlineCountryLabel = new JLabel();
        airlineCountryLabel.setText("country:");
        panel1.add(airlineCountryLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineCountryTextFiled = new JTextField();
        panel1.add(airlineCountryTextFiled, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        airlineIdValueLabel = new JLabel();
        airlineIdValueLabel.setText("");
        panel1.add(airlineIdValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineUpdateBtn = new JButton();
        airlineUpdateBtn.setText("Update");
        panel1.add(airlineUpdateBtn, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineRemoveBtn = new JButton();
        airlineRemoveBtn.setText("Remove");
        panel1.add(airlineRemoveBtn, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        flightTab = new JPanel();
        flightTab.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Flights Data", flightTab);
        flightTableWrapper = new JScrollPane();
        flightTab.add(flightTableWrapper, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        flightTable = new JTable();
        flightTableWrapper.setViewportView(flightTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        flightTab.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        flightIdLabel = new JLabel();
        flightIdLabel.setText("id:");
        panel2.add(flightIdLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightIdValueLabel = new JLabel();
        flightIdValueLabel.setText("");
        panel2.add(flightIdValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightAirlineIdLabel = new JLabel();
        flightAirlineIdLabel.setText("airline:");
        panel2.add(flightAirlineIdLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightAirlineComboBox = new JComboBox();
        panel2.add(flightAirlineComboBox, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightDepartureLabel = new JLabel();
        flightDepartureLabel.setText("departure:");
        panel2.add(flightDepartureLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightDepartureTextField = new JTextField();
        panel2.add(flightDepartureTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightArrivalLabel = new JLabel();
        flightArrivalLabel.setText("arrival:");
        panel2.add(flightArrivalLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightArrivalTextField = new JTextField();
        panel2.add(flightArrivalTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightPriceLabel = new JLabel();
        flightPriceLabel.setText("price:");
        panel2.add(flightPriceLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightProceSpinner = new JSpinner();
        panel2.add(flightProceSpinner, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightDepartureTimeLabel = new JLabel();
        flightDepartureTimeLabel.setText("datetime:");
        panel2.add(flightDepartureTimeLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightDepartureTimeFormattedTextField = new JFormattedTextField();
        panel2.add(flightDepartureTimeFormattedTextField, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightUpdateBtn = new JButton();
        flightUpdateBtn.setText("Update");
        panel2.add(flightUpdateBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightRemoveBtn = new JButton();
        flightRemoveBtn.setText("Remove");
        panel2.add(flightRemoveBtn, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        createTab = new JPanel();
        createTab.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Create", createTab);
        airlineNameLabel.setLabelFor(airlineNameTextField);
        airlineCountryLabel.setLabelFor(airlineCountryTextFiled);
        flightAirlineIdLabel.setLabelFor(flightAirlineComboBox);
        flightDepartureLabel.setLabelFor(flightDepartureTextField);
        flightArrivalLabel.setLabelFor(flightArrivalTextField);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
