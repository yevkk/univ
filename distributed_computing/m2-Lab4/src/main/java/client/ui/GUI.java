package client.ui;

import client.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Logger;

import com.formdev.flatlaf.FlatIntelliJLaf;
import entites.Airline;
import entites.Flight;
import lombok.SneakyThrows;

public class GUI extends JFrame {
    private Client client;
    private Airline selectedAirline = null;
    private Flight selectedFlight = null;
    private final Logger logger = Logger.getLogger(GUI.class.getName());

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

    public GUI() throws IOException {

        client = new Client();

        setContentPane(rootPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 500));

        flightPriceSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 50000.0, 0.1));
        flightCreatePriceSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 50000.0, 0.1));

        initTable(airlineTable, new String[]{"id", "name", "country"});
        initTable(flightTable, new String[]{"id", "airline id", "from", "to", "price"});

        updateAirlinesTable(airlineTable);
        updateFlightTable(flightTable);

        airlineTable.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = airlineTable.rowAtPoint(e.getPoint());
                if (row != -1) {
                    var model = airlineTable.getModel();

                    int id = (int) model.getValueAt(row, 0);
                    selectedAirline = client.findAirline(id);

                    airlineIdValueLabel.setText(String.valueOf(id));
                    airlineNameTextField.setText(selectedAirline.getName());
                    airlineCountryTextFiled.setText(selectedAirline.getCountry());
                } else {
                    clearSelectedAirline();
                }
            }
        });

        flightTable.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = flightTable.rowAtPoint(e.getPoint());
                if (row != -1) {
                    var model = flightTable.getModel();

                    int id = (int) model.getValueAt(row, 0);
                    selectedFlight = client.findFlight(id);

                    flightIdValueLabel.setText(String.valueOf(id));
                    flightAirlineComboBox.setSelectedItem(selectedFlight.getAirlineId());
                    flightDepartureTextField.setText(selectedFlight.getFrom());
                    flightArrivalTextField.setText(selectedFlight.getTo());
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

            try {
                client.updateAirline(selectedAirline);
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }

            updateAirlinesTable(airlineTable);
        });

        flightUpdateBtn.addActionListener(e -> {
            if (selectedFlight == null) {
                return;
            }

            selectedFlight.setAirlineId((int) flightAirlineComboBox.getSelectedItem());
            selectedFlight.setFrom(flightDepartureTextField.getText());
            selectedFlight.setTo(flightArrivalTextField.getText());
            selectedFlight.setPrice((double) flightPriceSpinner.getValue());

            try {
                client.updateFlight(selectedFlight);
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }

            updateFlightTable(flightTable);
        });

        airlineRemoveBtn.addActionListener(e -> {
            if (selectedAirline == null) {
                return;
            }

            try {
                client.deleteAirline(selectedAirline.getId());
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }

            clearSelectedAirline();
            updateAirlinesTable(airlineTable);
            updateFlightTable(flightTable);
        });

        flightRemoveBtn.addActionListener(e -> {
            if (selectedFlight == null) {
                return;
            }

            try {
                client.deleteFlight(selectedFlight.getId());
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }

            clearSelectedFlight();
            updateFlightTable(flightTable);
        });

        airlineCreateBtn.addActionListener(e -> {
            var airline = new Airline();
            airline.setName(airlineCreateNameTextFiled.getText());
            airline.setCountry(airlineCreateCountryTextField.getText());

            airlineCreateNameTextFiled.setText("");
            airlineCreateCountryTextField.setText("");

            try {
                client.createAirline(airline);
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }
            updateAirlinesTable(airlineTable);
        });

        flightCreateBtn.addActionListener(e -> {
            var flight = new Flight();
            flight.setAirlineId((int) flightCreateAirlineComboBox.getSelectedItem());
            flight.setFrom(flightCreateDepartureTextField.getText());
            flight.setTo(flightCreateArrivalTextField.getText());
            flight.setPrice((double) flightCreatePriceSpinner.getValue());

            flightCreateAirlineComboBox.setSelectedIndex(0);
            flightCreateDepartureTextField.setText("");
            flightCreateArrivalTextField.setText("");
            flightCreatePriceSpinner.setValue(0);

            try {
                client.createFlight(flight);
            } catch (IOException exception) {
                logger.warning(exception.getMessage());
            }
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

        try {
            var list = client.findAllAirlines();
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
        } catch (IOException exception) {
            logger.warning(exception.getMessage());
        }
    }

    public void updateFlightTable(JTable table) {
        var model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);

        try {
            var list = client.findAllFlights();
            for (var item : list) {
                model.addRow(new Object[]{
                        item.getId(),
                        item.getAirlineId(),
                        item.getFrom(),
                        item.getTo(),
                        item.getPrice(),
                });
            }
        } catch (IOException exception) {
            logger.warning(exception.getMessage());
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

    public static void main(String[] args) throws IOException {
        FlatIntelliJLaf.setup();
        new GUI();
    }
}
