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

import de.artive.vgraph.visio.VisioShape;
import de.artive.vgraph.visio.VisioRectangle;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 1:15:51 PM To change this template use File | Settings
 * | File Templates.
 */
public class Node extends GraphElement {
  private VisioRectangle visioRectangle;

  public Node(String extID, String text) {
    super(extID, text);
  }

  @Override
  public VisioShape getVisioShape() {
    return getVisioRectangle();
  }

  public VisioRectangle getVisioRectangle() {
    return visioRectangle;
  }

  public void setVisioRectangle(VisioRectangle visioRectangle) {
    this.visioRectangle = visioRectangle;
  }
}
