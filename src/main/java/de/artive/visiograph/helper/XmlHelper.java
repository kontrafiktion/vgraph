package de.artive.visiograph.helper;

import de.artive.visiograph.VisioGraphException;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 6:03:09 PM To change this template use File | Settings
 * | File Templates.
 */
public class XmlHelper {
    public static final String VISIO_SCHEMA = "http://schemas.microsoft.com/visio/2003/core";
    public static final XPathContext VISIO_XPATH_CONTEXT = new XPathContext("v", VISIO_SCHEMA);



    public static String getValue(Element xmlNode, String xPath) {
        String result = null;
        if (xPath != null) {
            Nodes nodes = xmlNode.query(xPath, VISIO_XPATH_CONTEXT);
            if (nodes.size() > 1) {
                throw new VisioGraphException("more than one result for XPath: \"" + xPath + "\" in Element: " + xmlNode);
            }
            result = nodes.get(0).getValue();

        }
        return result;
    }



    /**
     * Helper method to retrieve an Element referenced by an xPath.
     *
     * @param xquery
     * @return
     * @throws VisioGraphException
     */
    public static Element getSingleElement(Element element, String xquery) throws VisioGraphException {
        Nodes nodes = element.query(xquery, VISIO_XPATH_CONTEXT);
        if (nodes.size() != 1) {
            throw new VisioGraphException("xquery: \"" + xquery + "\" returned more than one Node");
        }
        return (Element) nodes.get(0);
    }

    /**
     * Sets the given value as Text in the Element referenced by the given xPath
     *
     * @param xPath -- the path in the document, that references the element, where the value should be set
     * @param value -- the value to be set
     */
    public static void setElementText(Element element, String xPath, BigDecimal value) {
        setElementText(element, xPath, value.toString());
    }

    /**
     * Sets the given value as Text in the Element referenced by the given xPath
     *
     * @param xPath -- the path in the document, that references the element, where the value should be set
     * @param value -- the value to be set
     */
    public static void setElementText(Element currentElement, String xPath, String value) {
        Element element = getSingleElement(currentElement, xPath);
        element.removeChildren();
        element.appendChild(value);
    }


    public static void setAttribute(Element xmlNode, String attrName, String attrValue) {
        Attribute idAttribute = xmlNode.getAttribute(attrName);
        if ( idAttribute != null ) {
            idAttribute.setValue(attrValue);
        } else {
           xmlNode.addAttribute(new Attribute(attrName, attrValue));
        }
    }
}
