package xml;

import appdata.DataStorage;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMBuilder extends XMLBuilder {
    private final BaseHandler handler;
    private DocumentBuilder documentBuilder;
    private final Logger logger;

    public DOMBuilder(BaseHandler handler) {
        super();
        this.handler = handler;
        logger = Logger.getLogger(DOMBuilder.class.getName());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.warning("DOM parser config error");
        }
    }

    @Override
    public void buildDataStorage(String filename) {
        Document document;
        try {
            document = documentBuilder.parse(filename);
            Element root = document.getDocumentElement();
            NodeList mainElementsList = root.getElementsByTagName(handler.mainElementName());
            for (int i = 0; i < mainElementsList.getLength(); i++) {
                handler.createMainElement();
                Element element = (Element) mainElementsList.item(i);
                for (String str : handler.attrs) {
                    handler.proceedElement(str, element.getAttribute(str));
                }
                buildElement(element);
                handler.saveMainElement();
            }
        } catch (SAXException e) {
            logger.warning("SAX parser error");
        } catch (IOException e) {
            logger.warning("I/O stream error");
        }
        dataStorage = handler.getDataStorage();
    }

    private void buildElement(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = childNodes.item(i).getNodeName();
            if (handler.complexElements.contains(childName)) {
                buildElement((Element) childNodes.item(i));
            } else {
                for (String str : getElementsTextContent(element, childName)){
                    handler.proceedElement(childName, str);
                }
            }
        }
    }

    private static List<String> getElementsTextContent(Element parent, String elementName) {
        NodeList nodeList = parent.getElementsByTagName(elementName);
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            strList.add(nodeList.item(i).getTextContent());
        }
        return strList;
    }

    public void saveDataStorage(String filename) {
        dataStorage = handler.getDataStorage();

        var doc = documentBuilder.newDocument();
        var root = doc.createElement(handler.rootElementName());
        doc.appendChild(root);

        for (int i = 0; i < dataStorage.getAll().size(); i++) {
            var element = doc.createElement(handler.mainElementName());
            handler.proceedSavingElement(doc, element, i);
            root.appendChild(element);
        }

        try {
            var domSource = new DOMSource(doc);
            var resFile = new StreamResult(new File(filename));
            var factory = TransformerFactory.newInstance();

            var transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(domSource, resFile);
        } catch (TransformerException e) {
            logger.warning(e.getMessage());
        }
    }
}