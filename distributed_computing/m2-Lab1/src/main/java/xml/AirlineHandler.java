package xml;

import appdata.AirlineData;
import appdata.DataStorage;
import entities.Airline;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;

public class AirlineHandler extends BaseHandler {
    private enum Elements {
        AIRLINES,
        AIRLINE,
        ID,
        NAME,
        COUNTRY
    }

    private final AirlineData data;
    private Airline current = null;
    private Elements currentElement = null;
    private final EnumSet<Elements> withText;

    public AirlineHandler() {
        this(new AirlineData());
    }

    public AirlineHandler(AirlineData data) {
        super();
        this.data = data;
        withText = EnumSet.range(Elements.NAME, Elements.COUNTRY);
        attrs = new ArrayList<>(Collections.singletonList("id"));
        complexElements = new ArrayList<>();
    }

    @Override
    public String rootElementName() {
        return "airlines";
    }

    @Override
    public String mainElementName() {
        return "airline";
    }

    @Override
    public DataStorage<?> getDataStorage() {
        data.correctMaxId();
        return data;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("airline")) {
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
        if (localName.equals("airline")) {
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
            case ID -> current.setId(Integer.parseInt(elemValue));
            case NAME -> current.setName(elemValue);
            case COUNTRY -> current.setCountry(elemValue);
        }
    }

    @Override
    public void createMainElement() {
        current = new Airline();
    }

    @Override
    public void saveMainElement() {
        data.add(current);
    }

    @Override
    public void proceedSavingElement(Document document, Element element, int index) {
        var airline = data.getAll().get(index);
        element.setAttribute("id", String.valueOf(airline.getId()));

        var name = document.createElement("name");
        name.setTextContent(airline.getName());
        element.appendChild(name);

        var country = document.createElement("country");
        country.setTextContent(airline.getCountry());
        element.appendChild(country);
    }
}
