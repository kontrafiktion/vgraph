package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 8:04:38 PM To change this template use File | Settings
 * | File Templates.
 */
public class SimpleXMLGraphXPathProvider implements GraphXPathProvider {
  @Override
  public String getNodesPath() {
    return "/graph/systems/system";
  }

  @Override
  public String getEdgesPath() {
    return "/graph/connections/connect";
  }

  @Override
  public String getNodeExtIdPath() {
    return "@id";
  }

  @Override
  public String getNodeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeExtIdPath() {
    return "@id";
  }

  @Override
  public String getEdgeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeSourceExtIdPath() {
    return "@source";
  }

  @Override
  public String getEdgeTargetExtIdPath() {
    return "@target";
  }
}
