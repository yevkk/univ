package ui;

import appdata.AppData;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import com.formdev.flatlaf.FlatIntelliJLaf;
import entities.Airline;
import entities.Flight;

public class GUI extends JFrame {
    private AppData appdata;
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
    private JComboBox<Integer> flightAirlineComboBox;
    private JTextField flightDepartureTextField;
    private JTextField flightArrivalTextField;
    private JSpinner flightPriceSpinner;
    private JLabel flightIdLabel;
    private JLabel flightIdValueLabel;
    private JLabel flightAirlineIdLabel;
    private JLabel flightDepartureLabel;
    private JLabel flightArrivalLabel;
    private JLabel flightPriceLabel;
    private JButton flightUpdateBtn;
    private JButton flightRemoveBtn;
    private JPanel createAirlinePanel;
    private JTextField airlineCreateNameTextFiled;
    private JTextField airlineCreateCountryTextField;
    private JButton airlineCreateBtn;
    private JLabel airlineCreateNameLabel;
    private JLabel airlineCreateCountryLabel;
    private JComboBox<Integer> flightCreateAirlineComboBox;
    private JTextField flightCreateDepartureTextField;
    private JTextField flightCreateArrivalTextField;
    private JSpinner flightCreatePriceSpinner;
    private JButton flightCreateBtn;
    private JLabel flightCreateAirlineLabel;
    private JLabel flightCreateDepartureLabel;
    private JLabel flightCreateArrivalLabel;
    private JLabel flightCreatePriceLabel;

    public GUI() {
        $$$setupUI$$$();

        setContentPane(rootPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 500));

        var menubar = new JMenuBar();
        var menu = new JMenu("data");
        var loadMenuItem = new JMenuItem("load");
        var saveMenuItem = new JMenuItem("save");

        loadMenuItem.addActionListener(e -> {
            var resource = ResourceBundle.getBundle("xml");
            appdata = AppData.loadFromFile(resource.getString("filename.airlines"), resource.getString("filename.flights"));

            updateAirlinesTable(airlineTable);
            updateFlightTable(flightTable);
        });

        saveMenuItem.addActionListener(e -> {
            var resource = ResourceBundle.getBundle("xml");
            appdata.saveToFile(resource.getString("filename.airlines"), resource.getString("filename.flights"));
        });

        menu.add(loadMenuItem);
        menu.add(saveMenuItem);
        menubar.add(menu);
        setJMenuBar(menubar);

        flightPriceSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 50000.0, 0.1));
        flightCreatePriceSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 50000.0, 0.1));

        initTable(airlineTable, new String[]{"id", "name", "country"});
        initTable(flightTable, new String[]{"id", "airline id", "from", "to", "price"});

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

        flightTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = flightTable.rowAtPoint(e.getPoint());
                if (row != -1) {
                    var model = flightTable.getModel();

                    int id = (int) model.getValueAt(row, 0);
                    var tmp = appdata.getFlightData().get(id);
                    selectedFlight = new Flight(id, tmp.getAirlineId(), tmp.getDepartureAirport(), tmp.getArrivalAirport(), tmp.getPrice());

                    flightIdValueLabel.setText(String.valueOf(id));
                    flightAirlineComboBox.setSelectedItem(selectedFlight.getAirlineId());
                    flightDepartureTextField.setText(selectedFlight.getDepartureAirport());
                    flightArrivalTextField.setText(selectedFlight.getArrivalAirport());
                    flightPriceSpinner.setValue(selectedFlight.getPrice());
                } else {
                    clearSelectedFlight();
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

        flightUpdateBtn.addActionListener(e -> {
            if (selectedFlight == null) {
                return;
            }

            selectedFlight.setAirlineId((int) flightAirlineComboBox.getSelectedItem());
            selectedFlight.setDepartureAirport(flightDepartureTextField.getText());
            selectedFlight.setArrivalAirport(flightArrivalTextField.getText());
            selectedFlight.setPrice((double) flightPriceSpinner.getValue());

            appdata.getFlightData().update(selectedFlight);

            updateFlightTable(flightTable);
        });

        airlineRemoveBtn.addActionListener(e -> {
            if (selectedAirline == null) {
                return;
            }

            appdata.getAirlineData().remove(selectedAirline.getId());

            clearSelectedAirline();
            updateAirlinesTable(airlineTable);
        });

        flightRemoveBtn.addActionListener(e -> {
            if (selectedFlight == null) {
                return;
            }

            appdata.getFlightData().remove(selectedFlight.getId());

            clearSelectedFlight();
            updateFlightTable(flightTable);
        });

        airlineCreateBtn.addActionListener(e -> {
            var airline = new Airline();
            airline.setName(airlineCreateNameTextFiled.getText());
            airline.setCountry(airlineCreateCountryTextField.getText());

            airlineCreateNameTextFiled.setText("");
            airlineCreateCountryTextField.setText("");

            appdata.getAirlineData().add(airline);
            updateAirlinesTable(airlineTable);
        });

        flightCreateBtn.addActionListener(e -> {
            var flight = new Flight();
            flight.setAirlineId((int) flightCreateAirlineComboBox.getSelectedItem());
            flight.setDepartureAirport(flightCreateDepartureTextField.getText());
            flight.setArrivalAirport(flightCreateArrivalTextField.getText());
            flight.setPrice((double) flightCreatePriceSpinner.getValue());

            flightCreateAirlineComboBox.setSelectedIndex(0);
            flightCreateDepartureTextField.setText("");
            flightCreateArrivalTextField.setText("");
            flightCreatePriceSpinner.setValue(0);

            appdata.getFlightData().add(flight);
            updateFlightTable(flightTable);
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
        Integer[] airlineIDs = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            var item = list.get(i);
            airlineIDs[i] = item.getId();
            model.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getCountry()
            });
        }
        flightAirlineComboBox.setModel(new DefaultComboBoxModel<>(airlineIDs));
        flightCreateAirlineComboBox.setModel(new DefaultComboBoxModel<>(airlineIDs));
    }

    public void updateFlightTable(JTable table) {
        var model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);

        var list = appdata.getFlightData().getAll();
        for (var item : list) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getAirlineId(),
                    item.getDepartureAirport(),
                    item.getArrivalAirport(),
                    item.getPrice(),
            });
        }
    }

    private void clearSelectedAirline() {
        selectedAirline = null;
        airlineIdValueLabel.setText("");
        airlineNameTextField.setText("");
        airlineCountryTextFiled.setText("");
    }

    private void clearSelectedFlight() {
        selectedFlight = null;
        flightIdValueLabel.setText("");
        flightAirlineComboBox.setSelectedIndex(0);
        flightDepartureTextField.setText("");
        flightArrivalTextField.setText("");
        flightPriceSpinner.setValue(0.0);
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
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
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
        flightPriceSpinner = new JSpinner();
        panel2.add(flightPriceSpinner, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightUpdateBtn = new JButton();
        flightUpdateBtn.setText("Update");
        panel2.add(flightUpdateBtn, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightRemoveBtn = new JButton();
        flightRemoveBtn.setText("Remove");
        panel2.add(flightRemoveBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        createTab = new JPanel();
        createTab.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Create", createTab);
        createAirlinePanel = new JPanel();
        createAirlinePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 5, 0, 5), -1, -1));
        createTab.add(createAirlinePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createAirlinePanel.setBorder(BorderFactory.createTitledBorder(null, "Airline", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        airlineCreateNameLabel = new JLabel();
        airlineCreateNameLabel.setText("name:");
        createAirlinePanel.add(airlineCreateNameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineCreateNameTextFiled = new JTextField();
        createAirlinePanel.add(airlineCreateNameTextFiled, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        airlineCreateCountryLabel = new JLabel();
        airlineCreateCountryLabel.setText("country:");
        createAirlinePanel.add(airlineCreateCountryLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        airlineCreateCountryTextField = new JTextField();
        createAirlinePanel.add(airlineCreateCountryTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        airlineCreateBtn = new JButton();
        airlineCreateBtn.setText("Create");
        createAirlinePanel.add(airlineCreateBtn, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        createAirlinePanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 5, 0, 5), -1, -1));
        createTab.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Flight", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        flightCreateAirlineLabel = new JLabel();
        flightCreateAirlineLabel.setText("airline:");
        panel3.add(flightCreateAirlineLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        flightCreateAirlineComboBox = new JComboBox();
        panel3.add(flightCreateAirlineComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightCreateDepartureLabel = new JLabel();
        flightCreateDepartureLabel.setText("from:");
        panel3.add(flightCreateDepartureLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightCreateDepartureTextField = new JTextField();
        panel3.add(flightCreateDepartureTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightCreateArrivalLabel = new JLabel();
        flightCreateArrivalLabel.setText("to:");
        panel3.add(flightCreateArrivalLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightCreatePriceLabel = new JLabel();
        flightCreatePriceLabel.setText("price:");
        panel3.add(flightCreatePriceLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightCreateArrivalTextField = new JTextField();
        panel3.add(flightCreateArrivalTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flightCreatePriceSpinner = new JSpinner();
        panel3.add(flightCreatePriceSpinner, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightCreateBtn = new JButton();
        flightCreateBtn.setText("Create");
        panel3.add(flightCreateBtn, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        createTab.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        airlineNameLabel.setLabelFor(airlineNameTextField);
        airlineCountryLabel.setLabelFor(airlineCountryTextFiled);
        flightAirlineIdLabel.setLabelFor(flightAirlineComboBox);
        flightDepartureLabel.setLabelFor(flightDepartureTextField);
        flightArrivalLabel.setLabelFor(flightArrivalTextField);
        airlineCreateNameLabel.setLabelFor(airlineCreateNameTextFiled);
        airlineCreateCountryLabel.setLabelFor(airlineCreateCountryTextField);
        flightCreateAirlineLabel.setLabelFor(flightCreateAirlineComboBox);
        flightCreateDepartureLabel.setLabelFor(flightCreateDepartureTextField);
        flightCreateArrivalLabel.setLabelFor(flightCreateArrivalTextField);
        flightCreatePriceLabel.setLabelFor(flightCreatePriceSpinner);
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
