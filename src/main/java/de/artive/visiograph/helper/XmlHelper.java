/*
 * Copyright 2010 Victor Volle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.artive.visiograph.helper;

import de.artive.visiograph.VisioGraphException;
import nu.xom.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Helper methods for handling the XOM tree of a VisioDocument.
 * <p/>
 * All path elements in the {@link #VISIO_SCHEMA) should be preficed with the namespace abbreviation "v". I.e.
 * "/VisioDocument/Pages" should be used as "/v:VisioDocument/v:Pages"
 */
public class XmlHelper {
  public static final String VISIO_SCHEMA = "http://schemas.microsoft.com/visio/2003/core";
  public static final XPathContext VISIO_XPATH_CONTEXT = new XPathContext("v", VISIO_SCHEMA);


  /**
   * Retrieves an Element referenced by the given <code>xPath</code>
   * <p/>
   * This method is a simple wrapper around {@link #getSingleElement(nu.xom.Element, String, boolean)} just adding a
   * <code>false</code> as value for the 'optional' parameter
   *
   * @see #getSingleNode(nu.xom.Element, String, boolean)
   */
  public static Element getSingleElement(Element element, String xPath) throws VisioGraphException {
    return getSingleElement(element, xPath, false);
  }

  /**
   * Retrieves an Element referenced by the given <code>xPath</code>
   * <p/>
   * This method is a simple wrapper around {@link #getSingleNode(nu.xom.Element, String, boolean)} just eliminating the
   * cast.
   *
   * @see #getSingleNode(nu.xom.Element, String, boolean)
   */
  public static Element getSingleElement(Element element, String xPath, boolean optional) throws VisioGraphException {
    return (Element) getSingleNode(element, xPath, optional);
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

  /**
   * Helper method to retrieve a node by the given <code>xPath</code>.
   * <p/>
   * The <code>xPath</code> must reference a single Element or Attribute
   *
   * @param element  the xml element, which shall be used as root for the search
   * @param xPath    the XPath that references a single element/attribute
   * @param optional -- if true, no VisioGraphException is thrown if no referenced node was found
   * @return the value of the element/attribute
   * @throws VisioGraphException if more than one referenced node is found.
   * @throws VisioGraphException if <code>optional</code> is false and no referenced node is found.
   */
  public static Node getSingleNode(Element element, String xPath, boolean optional) {
    Nodes nodes = element.query(xPath, VISIO_XPATH_CONTEXT);
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


  /**
   * Helper method to retrieve a value by the given <code>xPath</code>.
   * <p/>
   * The <code>xPath</code> must reference a single Element or Attribute
   *
   * @param element the xml element, which shall be used as root for the search
   * @param xPath   the XPath that references a single element/attribute
   * @return the value of the element/attribute
   * @throws VisioGraphException if no or more than one referenced node is found
   */
  public static String getValue(Element element, String xPath) {
    return getValue(element, xPath, false);
  }


  /**
   * Helper method to retrieve a value by the given <code>xPath</code>.
   * <p/>
   * The <code>xPath</code> must reference a single Element or Attribute
   *
   * @param element  the xml element, which shall be used as root for the search
   * @param xPath    the XPath that references a single element/attribute
   * @param optional -- if true, no VisioGraphException is thrown if no referenced node was found
   * @return the value of the element/attribute
   * @throws VisioGraphException if more than one referenced node is found.
   * @throws VisioGraphException if <code>optional</code> is false and no referenced node is found.
   */
  public static String getValue(Element element, String xPath, boolean optional) {
    Node node = getSingleNode(element, xPath, optional);
    if (node != null) {
      return node.getValue();
    }
    return null;
  }

  /**
   * Helper method to change the value of an Element/Attribute in an XML tree
   * <p/>
   * The <code>xPath</code> must reference a single Element or Attribute
   *
   * @param element the xml element, which shall be used as root for the search
   * @param xPath   the XPath that references a single element/attribute
   * @param value   the new value of the referenced node (the BigDecimal is converted to a String using {@link
   *                java.math.BigDecimal#toPlainString()}
   */
  public static void setValue(Element element, String xPath, BigDecimal value) {
    setValue(element, xPath, value.toPlainString());
  }


  /**
   * Helper method to change the value of an Element/Attribute in an XML tree
   * <p/>
   * The <code>xPath</code> must reference a single Element or Attribute
   *
   * @param element the xml element, which shall be used as root for the search
   * @param xPath   the XPath that references a single element/attribute
   * @param value   the new value of the referenced node
   */
  public static void setValue(Element element, String xPath, String value) {
    Node node = getSingleNode(element, xPath, false);
    if (node instanceof Attribute) {
      ((Attribute) node).setValue(value);
    } else if (node instanceof Element) {
      Element foundElement = (Element) node;
      foundElement.removeChildren();
      foundElement.appendChild(value);
    }

  }


  public static void setAttribute(Element element, String attrName, String attrValue) {
    Attribute idAttribute = element.getAttribute(attrName);
    if (idAttribute != null) {
      idAttribute.setValue(attrValue);
    } else {
      element.addAttribute(new Attribute(attrName, attrValue));
    }
  }


  public static Document visioBuild(String xmlString) {
    Builder builder = new Builder();
    try {
      Document document = builder.build(xmlString, "");
      // don't know why this is necessary, but XOM uses the current working directory as default base uri
      document.setBaseURI("");
      return document;
    } catch (ParsingException e) {
      throw new VisioGraphException("error parsing: " + xmlString, e);
    } catch (IOException e) {
      throw new VisioGraphException("error parsing: " + xmlString, e);
    }
  }
}
