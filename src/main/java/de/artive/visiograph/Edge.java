package de.artive.visiograph;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 11, 2010
 * Time: 1:16:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Edge extends GraphElement {

    public Edge(String extID, String text, Node source, Node target) {
        super(extID, text);
        this.source = source;
        this.target = target;
    }

    private Node source;

    private Node target;

    private boolean directed;

    private Map<String, String> properties = new HashMap<String, String>();


    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

}
