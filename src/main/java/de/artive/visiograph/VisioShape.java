package de.artive.visiograph;

import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 15, 2010 Time: 8:13:26 AM To change this template use File | Settings
 * | File Templates.
 */
public abstract class VisioShape {
  public static final BigDecimal DEFAULT_VALUE = new BigDecimal("1.00000000000000");

  // TODO: document why "."
  public static final String _VS_SHAPE = ".";
  public static final String PROP_EXTERNAL_ID_VALUE = "/v:Prop[v:Label='external_id']/v:Value";
  public static final String _VS_EXT_ID = _VS_SHAPE + PROP_EXTERNAL_ID_VALUE;
  public static final String VS_VISIO_ID = "@ID";
  public static final String _VS_VISIO_ID = _VS_SHAPE + "/" + VS_VISIO_ID;

  public static final String _VS_XFORM = _VS_SHAPE + "/v:XForm";
  public static final String _VS_PINX = _VS_XFORM + "/v:PinX";
  public static final String _VS_PINY = _VS_XFORM + "/v:PinY";
  public static final String _VS_WIDTH = _VS_XFORM + "/v:Width";
  public static final String _VS_HEIGHT = _VS_XFORM + "/v:Height";
  public static final String _VS_TEXT = _VS_SHAPE + "/v:Text";

  public static final Element CHAR_INDEX_ELEMENT;

  static {
    CHAR_INDEX_ELEMENT = new Element("cp", XmlHelper.VISIO_SCHEMA);
    CHAR_INDEX_ELEMENT.addAttribute(new Attribute("IX", "0"));
  }


  protected VisioShape(Element xmlRoot) {
    this.xmlRoot = xmlRoot;
  }

  protected Element xmlRoot;

  protected VisioShape() {
    Builder builder = new Builder();
    try {
      Document document = builder.build(getTemplate(), "");
      xmlRoot = (Element) document.getRootElement().copy();
    } catch (ParsingException e) {
      throw new VisioGraphException("problem parsing: " + getTemplate(), e);
    } catch (IOException e) {
      throw new VisioGraphException("we are not doing IO here, why should there be an IO Exception:" + e.getMessage(),
                                    e);
    }
  }

  protected VisioShape(BigDecimal pinX, BigDecimal pinY, BigDecimal width, BigDecimal height, String extId, String text) {
    this();
    setPinX(pinX);
    setPinY(pinY);
    setWidth(width);
    setHeight(height);
    setExtId(extId);
    setText(text);
  }

  protected abstract String getTemplate();

  public int getVisioID() {
    Attribute visioIDAttr = (Attribute) xmlRoot.query(_VS_VISIO_ID, XmlHelper.VISIO_XPATH_CONTEXT).get(0);
    Integer visioID;
    try {
      visioID = Integer.valueOf(visioIDAttr.getValue());
    } catch (NumberFormatException e) {
      visioID = -1;
    }
    return visioID;
  }

  public void setVisioID(int visioID) {
    setValue(_VS_VISIO_ID, String.valueOf(visioID));
  }

  public BigDecimal getPinX() {
    return safeBigDecimal(getSingleElement(_VS_PINX).getValue());
  }

  public void setPinX(BigDecimal pinX) {
    setValue(_VS_PINX, pinX);
  }

  public BigDecimal getPinY() {
    return safeBigDecimal(getSingleElement(_VS_PINY).getValue());
  }

  public void setPinY(BigDecimal pinY) {
    setValue(_VS_PINY, pinY);
  }

  public BigDecimal getWidth() {
    return safeBigDecimal(getSingleElement(_VS_WIDTH).getValue());
  }

  public void setWidth(BigDecimal width) {
    setValue(_VS_WIDTH, width);
  }

  public BigDecimal getHeight() {
    return safeBigDecimal(getSingleElement(_VS_HEIGHT).getValue());
  }

  public void setHeight(BigDecimal height) {
    setValue(_VS_HEIGHT, height);
  }

  public String getExtId() {
    return getSingleElement(_VS_EXT_ID).getValue();
  }

  public void setExtId(String id) {
    setValue(_VS_EXT_ID, id);
  }

  public String getText() {
    return getSingleElement(_VS_TEXT).getValue();
  }

  public void setText(String text) {
    Element textElement = getSingleElement(_VS_TEXT);
    textElement.removeChildren();
    // TODO: better formatting
    // textElement.appendChild(CHAR_INDEX_ELEMENT.copy());
    textElement.appendChild(text);
  }

  protected Element getSingleElement(String text) {
    return XmlHelper.getSingleElement(xmlRoot, text);
  }

  /**
   * Converts a String into a BigDecimal. If the conversion throws a NumberFormatException the {@link #DEFAULT_VALUE} is
   * returned. The XML templates contain placeholders (e.g. "${PinX}) because these templates ought to be used by
   * simpler code using Velocity. Therefore retrieving this String and converting it would throw an Exception.
   *
   * @param value -- a String that can be converted to a BigDecimal
   * @return the converted value or {@link #DEFAULT_VALUE} if the String could not be converted
   */
  public static BigDecimal safeBigDecimal(String value) {
    BigDecimal result;
    try {
      result = new BigDecimal(value);
    } catch (NumberFormatException e) {
      result = VisioRectangle.DEFAULT_VALUE;
    }
    return result;
  }

  /**
   * Sets the given value as Text in the Element referenced by the given xPath
   *
   * @param xPath -- the path in the document, that references the element, where the value should be set
   * @param value -- the value to be set
   */
  protected void setValue(String xPath, BigDecimal value) {
    setValue(xPath, value.toString());
  }

  /**
   * Sets the given value as Text in the Element referenced by the given xPath
   *
   * @param xPath -- the path in the document, that references the element, where the value should be set
   * @param value -- the value to be set
   */
  protected void setValue(String xPath, String value) {
    XmlHelper.setValue(xmlRoot, xPath, value);
  }

  public Element getShapeElement() {
    return xmlRoot;
  }

  public void setShapeElement(Element xmlRoot) {
    this.xmlRoot = xmlRoot;
  }


  @Override
  public String toString() {
    return getText() + " [" + getExtId() + "]";
  }
}
