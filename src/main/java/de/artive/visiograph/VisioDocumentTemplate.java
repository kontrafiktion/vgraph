package de.artive.visiograph;

import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;
import nu.xom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static de.artive.visiograph.helper.XmlHelper.getValue;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 4:20:50 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioDocumentTemplate {

    public static final BigDecimal INCH = new BigDecimal("25.4");

    public static final String DEFAULT_VISIO_TEMPLATE = "visio_blank_a4_landscape.vdx";

    public static final String _ROOT = "/v:VisioDocument";
    public static final String _PAGE = _ROOT + "/v:Pages/v:Page[@ID=\"0\"]";
    public static final String _PAGE_SHEET = _PAGE + "/v:PageSheet";
    public static final String _PAGE_PROPERTIES = _PAGE_SHEET + "/v:PageProps";
    public static final String _PAGE_WIDTH = _PAGE_PROPERTIES + "/v:PageWidth";
    public static final String _PAGE_HEIGHT = _PAGE_PROPERTIES + "/v:PageHeight";
    public static final String _SHAPES_CONTAINER = _PAGE + "/v:Shapes";
    public static final String _CONNECTS_CONTAINER = _PAGE + "/v:Connects";
    public static final String MAX_SHAPE_ID = "v:Shape[not(following-sibling::v:Shape/@ID > @ID) and not(preceding-sibling::v:Shape/@ID > @ID)]/@ID";


    int nextShapeId = 0;

    private Document document = null;
    private Element shapesContainer;

    public VisioDocumentTemplate() throws VisioGraphException {
        Builder builder = new Builder();
        InputStream docStream = null;
        docStream = VisioDocumentTemplate.class.getClassLoader().getResourceAsStream(DEFAULT_VISIO_TEMPLATE);
        Exception originalException = null;
        try {
            document = builder.build(docStream);
        } catch (ParsingException e) {
            originalException = e;
            throw new VisioGraphException("error parsing: " + DEFAULT_VISIO_TEMPLATE, e);
        } catch (IOException e) {
            originalException = e;
            throw new VisioGraphException("could not load: " + DEFAULT_VISIO_TEMPLATE, e);
        } finally {
            try {
                docStream.close();
            } catch (IOException e) {
                if (originalException == null) {
                    throw new VisioGraphException("error closing stream for: " + DEFAULT_VISIO_TEMPLATE, e);
                } else {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        VisioDocumentTemplate visioDocumentTemplate = new VisioDocumentTemplate();
        visioDocumentTemplate.addShape(null);
    }

    public void addShape(Element xmlNode) {
        if (shapesContainer == null) {
            Nodes nodes = document.getRootElement().query(_SHAPES_CONTAINER, XmlHelper.VISIO_XPATH_CONTEXT);
            if (nodes.size() == 0) {
                Element page = XmlHelper.getSingleElement(document.getRootElement(), _PAGE);
                Element pageSheet = XmlHelper.getSingleElement(document.getRootElement(), _PAGE_SHEET);
                int posPageSheet = page.indexOf(pageSheet);
                shapesContainer = new Element("Shapes", XmlHelper.VISIO_SCHEMA);
                page.insertChild(shapesContainer, posPageSheet + 1);
            } else {
                shapesContainer = (Element) nodes.get(0);
                Node maxShapeId = shapesContainer.query(MAX_SHAPE_ID, XmlHelper.VISIO_XPATH_CONTEXT).get(0);
                if (maxShapeId != null) {
                    nextShapeId = Integer.valueOf(maxShapeId.getValue()) + 1;
                }
            }
        }
        xmlNode = new Element("Shoop", XmlHelper.VISIO_SCHEMA);
        String attrName = "ID";
        String attrValue = String.valueOf(nextShapeId);
        XmlHelper.setAttribute(xmlNode, attrName, attrValue);

        nextShapeId++;
        shapesContainer.appendChild(xmlNode);
        System.out.println(document.toXML());
    }

    public BigDecimal getPageWidth() {
        String value = getValue(document.getRootElement(), _PAGE_WIDTH);
        if (value == null) {
            throw new VisioGraphException("could not determine page width: " + _PAGE_WIDTH);
        }
        return new BigDecimal(value).multiply(INCH);
    }

    public BigDecimal getPageHeight() {
        String value = getValue(document.getRootElement(), _PAGE_HEIGHT);
        if (value == null) {
            throw new VisioGraphException("could not determine page width: " + _PAGE_HEIGHT);
        }
        return new BigDecimal(value).multiply(INCH);
    }

}
