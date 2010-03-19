package de.artive.vgraph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Mar 1, 2010 Time: 11:03:07 AM To change this template use File | Settings
 * | File Templates.
 */
public class XmlNodeNotFoundException extends VGraphException {

  public XmlNodeNotFoundException(String message) {
    super(ErrorCode.XML_NODE_NOT_FOUND, message);
  }

  public XmlNodeNotFoundException(String message, Throwable cause) {
    super(ErrorCode.XML_NODE_NOT_FOUND, message, cause);
  }
}
