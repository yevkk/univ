package xml;

import appdata.DataStorage;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public abstract class BaseHandler extends DefaultHandler {
    protected ArrayList<String> attrs;
    protected ArrayList<String> complexElements;

    public abstract String mainElementName();

    public abstract DataStorage<?> getDataStorage();

    public abstract void proceedElement(String elemLocalName, String elemValue);

    public abstract void createMainElement();

    public abstract void saveMainElement();
}