package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 6:57:06 PM To change this template use File | Settings
 * | File Templates.
 */
public interface GraphXPathProvider {

  String getNodesPath();

  String getEdgesPath();

  String getNodeExtIdPath();

  String getNodeTextPath();

  String getEdgeExtIdPath();

  String getEdgeTextPath();

  String getEdgeSourceExtIdPath();

  String getEdgeTargetExtIdPath();

}
