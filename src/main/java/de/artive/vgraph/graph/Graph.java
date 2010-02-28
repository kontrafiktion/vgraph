/*
 * Copyright (c) 2010 Victor Volle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Contributors:
 *      Victor Volle
 */

package de.artive.vgraph.graph;

import de.artive.vgraph.VGraphException;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 12, 2010 Time: 8:44:13 PM To change this template use File | Settings
 * | File Templates.
 */
public class Graph {

  private Map<String, Node> nodes = new HashMap<String, Node>();
  private Map<String, Edge> edges = new HashMap<String, Edge>();

  public void addNode(String extId, String text) {
    addNode(new Node(extId, text));
  }

  public void addNode(Node node) {
    if (node.getExtID() == null) {
      throw new VGraphException("no external ID given: " + node.getText());
    }
    checkUniqueExtId(node);
    nodes.put(node.getExtID(), node);
  }

  private void checkUniqueExtId(GraphElement graphElement) {
    if (nodes.containsKey(graphElement.getExtID()) || edges.containsKey(graphElement.getExtID())) {
      throw new VGraphException("Duplicate external ID: " + graphElement.getExtID());
    }
  }

  public boolean removeNode(Node node) {
    return nodes.remove(node.getExtID()) != null;
  }

  public Node getNode(String extId) {
    return nodes.get(extId);
  }

  public Collection<Node> getNodes() {
    return nodes.values();
  }

  public Collection<Edge> getEdges() {
    return edges.values();
  }


  public void addEdge(Edge edge) {
    checkUniqueExtId(edge);
    edges.put(edge.getExtID(), edge);
  }

  public Edge getEdge(String extId) {
    return edges.get(extId);
  }


}
