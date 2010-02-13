package de.artive.visiograph;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 13, 2010
 * Time: 6:57:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphXPathProvider {

    String getNodes();
    String getEdges();
    String getNodeExtId();
    String getNodeText();
    String getEdgeExtId();
    String getEdgeText();
    String getEdgeSourceExtId();
    String getEdgeTargetExtId();

}
