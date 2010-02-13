package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 13, 2010
 * Time: 8:04:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleXMLGraphXPathProvider implements GraphXPathProvider {
    @Override
    public String getNodes() {
        return "/graph/systems/system";
    }

    @Override
    public String getEdges() {
        return "/graph/connections/connect";
    }

    @Override
    public String getNodeExtId() {
        return "@id";
    }

    @Override
    public String getNodeText() {
        return ".";
    }

    @Override
    public String getEdgeExtId() {
        return "@id";
    }

    @Override
    public String getEdgeText() {
        return ".";
    }

    @Override
    public String getEdgeSourceExtId() {
        return "@source";
    }

    @Override
    public String getEdgeTargetExtId() {
        return "@target";
    }
}
