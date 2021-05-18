package xml;

import appdata.DataStorage;
import appdata.FlightData;
import entities.Airline;
import entities.Flight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

public class FlightHandler extends BaseHandler {
    private enum Elements {
        FLIGHTS,
        FLIGHT,
        ID,
        AIRLINE_ID,
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
        this(new FlightData());
    }

    public FlightHandler(FlightData data) {
        super();
        this.data = data;
        withText = EnumSet.range(Elements.DEPARTURE, Elements.DEPARTURE_TIMESTAMP);
        attrs = new ArrayList<>(Arrays.asList("id", "airline_id"));
        complexElements = new ArrayList<>();
    }

    @Override
    public String rootElementName() {
        return "flights";
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
            current.setAirlineId(Integer.parseInt(attributes.getValue(1)));
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

    @Override
    public void proceedSavingElement(Document document, Element element, int index) {
        var flight = data.getAll().get(index);
        element.setAttribute("id", String.valueOf(flight.getId()));

        var airlineId = document.createElement("airline_id");
        airlineId.setTextContent( String.valueOf(flight.getAirlineId()));
        element.appendChild(airlineId);

        var departure = document.createElement("departure");
        departure.setTextContent(flight.getDepartureAirport());
        element.appendChild(departure);

        var arrival = document.createElement("arrival");
        arrival.setTextContent(flight.getArrivalAirport());
        element.appendChild(arrival);

        var price = document.createElement("price");
        price.setTextContent(String.valueOf(flight.getPrice()));
        element.appendChild(price);

        var departureDateTime = document.createElement("departure_timestamp");
        departureDateTime.setTextContent(flight.getDepartureDateTime().toString());
        element.appendChild(departureDateTime);
    }
}
