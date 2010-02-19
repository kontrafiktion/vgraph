package de.artive.visiograph.helper;

import de.artive.visiograph.VisioGraphException;
import nu.xom.*;

import java.math.BigDecimal;

/**
 * FIXME: clean up helper methods
 * <p/>
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
    if (nodes.size() > 1) {
      throw new VisioGraphException("xquery: \"" + xquery + "\" returned more than one Node");
    } else if (nodes.size() == 0) {
      throw new VisioGraphException("xquery: \"" + xquery + "\" found no Node");
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
    setValue(element, xPath, value.toString());
  }

  public static Node getSingleNode(Element xmlNode, String xPath, boolean optional) {
    Nodes nodes = xmlNode.query(xPath, VISIO_XPATH_CONTEXT);
    if (nodes.size() > 1) {
      throw new VisioGraphException("xPath: \"" + xPath + "\" returned more than one Node");
    } else if (nodes.size() == 0) {
      if (!optional) {
        throw new VisioGraphException("xPath: \"" + xPath + "\" found no Node");
      } else {
        return null;
      }
    }
    return nodes.get(0);
  }

  public static String getValue(Element xmlNode, String xPath, boolean optional) {
    Node node = getSingleNode(xmlNode, xPath, optional);
    if (node != null) {
      return node.getValue();
    }
    return null;
  }

  public static void setValue(Element xmlNode, String xPath, BigDecimal value) {
    setValue(xmlNode, xPath, value.toPlainString());
  }


  public static void setValue(Element xmlNode, String xPath, String value) {
    Node node = getSingleNode(xmlNode, xPath, false);
    if (node instanceof Attribute) {
      ((Attribute) node).setValue(value);
    } else if (node instanceof Element) {
      Element element = (Element) node;
      element.removeChildren();
      element.appendChild(value);
    }

  }


  public static void setAttribute(Element xmlNode, String attrName, String attrValue) {
    Attribute idAttribute = xmlNode.getAttribute(attrName);
    if (idAttribute != null) {
      idAttribute.setValue(attrValue);
    } else {
      xmlNode.addAttribute(new Attribute(attrName, attrValue));
    }
  }
}
