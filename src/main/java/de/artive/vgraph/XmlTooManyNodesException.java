package de.artive.vgraph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Mar 1, 2010 Time: 11:03:07 AM To change this template use File | Settings
 * | File Templates.
 */
public class XmlTooManyNodesException extends VGraphException {

  public XmlTooManyNodesException(String message) {
    super(ErrorCode.XML_TOO_MANY_NODES, message);
  }

  public XmlTooManyNodesException(String message, Throwable cause) {
    super(ErrorCode.XML_TOO_MANY_NODES, message, cause);
  }
}