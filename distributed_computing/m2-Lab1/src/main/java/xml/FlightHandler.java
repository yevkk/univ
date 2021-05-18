package xml;

import appdata.DataStorage;
import appdata.FlightData;
import entities.Airline;
import entities.Flight;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.time.ZonedDateTime;
import java.util.EnumSet;

public class FlightHandler extends BaseHandler {
    private enum Elements {
        FLIGHTS,
        FLIGHT,
        ID,
        DEPARTURE,
        ARRIVAL,
        PRICE,
        DEPARTURE_TIMESTAMP
    }

    private final FlightData data;
    private Flight current = null;
    private Elements currentElement = null;
    private final EnumSet<Elements> withText;

    public FlightHandler() {
        super();
        this.data = new FlightData();
        withText = EnumSet.range(Elements.DEPARTURE, Elements.DEPARTURE_TIMESTAMP);
    }

    @Override
    public String mainElementName() {
        return "flight";
    }

    @Override
    public DataStorage<?> getDataStorage() {
        return data;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("flight")) {
            createMainElement();
            current.setId(Integer.parseInt(attributes.getValue(0)));
        } else {
            var tmp = Elements.valueOf(localName.toUpperCase());
            if (withText.contains(tmp)) {
                currentElement = tmp;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("flight")) {
            saveMainElement();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        var str = new String(ch, start, length).trim();
        if (currentElement != null) {
            proceedElement(currentElement.toString(), str);
        }
        currentElement = null;
    }

    @Override
    public void proceedElement(String elemLocalName, String elemValue) {
        var element = Elements.valueOf(elemLocalName.toUpperCase());
        switch (element) {
            case DEPARTURE -> current.setDepartureAirport(elemValue);
            case ARRIVAL -> current.setArrivalAirport(elemValue);
            case PRICE -> current.setPrice(Double.parseDouble(elemValue));
            case DEPARTURE_TIMESTAMP -> current.setDepartureDateTime(ZonedDateTime.parse(elemValue));
        }
    }

    @Override
    public void createMainElement() {
        current = new Flight();
    }

    @Override
    public void saveMainElement() {
        data.add(current);
    }
}
