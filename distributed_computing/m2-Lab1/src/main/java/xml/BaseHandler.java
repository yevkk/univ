package xml;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHandler extends DefaultHandler {
    protected ArrayList<String> attrs;
    protected ArrayList<String> complexElements;

    public abstract String mainElementName();

    public abstract List<?> getList();

    public abstract void proceedElement(String elemLocalName, String elemValue);

    public abstract void createMainElement();

    public abstract void saveMainElement();
}