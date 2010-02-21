/*
 * Copyright 2010 Victor Volle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
