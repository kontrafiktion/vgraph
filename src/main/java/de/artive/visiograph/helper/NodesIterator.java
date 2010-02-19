package de.artive.visiograph.helper;

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
