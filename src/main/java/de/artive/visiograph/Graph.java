package de.artive.visiograph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 12, 2010
 * Time: 8:44:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Graph {
    
    private Map<String, Node> nodes = new HashMap<String, Node>();
    private Map<String, Edge> edges = new HashMap<String, Edge>();

    public void addNode(String extId, String text) {
        addNode(new Node(extId, text));
    }

    public void addNode(Node node) {
        checkUniqueExtId(node);
        nodes.put(node.getExtID(), node);
    }

    private void checkUniqueExtId(GraphElement graphElement) {
        if ( nodes.containsKey(graphElement.getExtID()) || edges.containsKey(graphElement.getExtID()) ) {
            throw new VisioGraphException("Duplicate external ID: " + graphElement.getExtID());
        }
    }

    public boolean removeNode(Node node) {
        return nodes.remove(node.getExtID()) != null;
    }

    public Node getNode(String extId) {
        return nodes.get(extId);
    }

    public void addEdge(Edge edge) {
        checkUniqueExtId(edge);
        edges.put(edge.getExtID(), edge);
    }


}
