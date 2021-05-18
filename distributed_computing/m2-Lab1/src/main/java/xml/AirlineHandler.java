package xml;

import entities.Airline;
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

    private final List<Airline> list;
    private Airline current = null;
    private Elements currentElement = null;
    private final EnumSet<Elements> withText;

    public AirlineHandler() {
        super();
        this.list = new ArrayList<>();
        withText = EnumSet.range(Elements.NAME, Elements.COUNTRY);
        attrs = new ArrayList<>(Collections.singletonList("id"));
    }

    @Override
    public String mainElementName() {
        return "airline";
    }

    @Override
    public List<?> getList() {
        return list;
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
        list.add(current);
    }
}
