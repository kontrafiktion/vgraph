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

package de.artive.vgraph.helper;

import nu.xom.Node;
import nu.xom.Nodes;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 7:28:04 PM To change this template use File | Settings
 * | File Templates.
 */
public class NodesIterator implements Iterator<Node> {

  private Nodes nodes;
  private int pos = 0;

  public NodesIterator(Nodes nodes) {
    this.nodes = nodes;
  }

  public static Iterator<Node> nodesIterator(Nodes nodes) {
    return new NodesIterator(nodes);
  }


  @Override
  public boolean hasNext() {
    return nodes.size() > pos;
  }

  @Override
  public Node next() {
    Node result = nodes.get(pos);
    pos++;
    return result;
  }

  @Override
  public void remove() {
    nodes.remove(pos - 1);
  }
}
