import appdata.AppData;
import entities.Airline;
import ui.GUI;

import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
         ResourceBundle resource = ResourceBundle.getBundle("xml");

        var appdata = AppData.loadFromFile(resource.getString("filename.airlines"), resource.getString("filename.flights"));

        var list1 = appdata.getAirlineData().getAll();
        var list2 = appdata.getFlightData().getAll();

        var logger = Logger.getLogger(Main.class.getName());
        for (var a : list1) {
            logger.info(a.getName());
        }
        for (var a : list2) {
            logger.info(a.getDepartureDateTime().toString());
        }

        appdata.getAirlineData().add(new Airline(1, "MAU", "UA"));

        appdata.saveToFile(resource.getString("filename.airlines"), resource.getString("filename.flights"));

        GUI.main(null);
    }
}
