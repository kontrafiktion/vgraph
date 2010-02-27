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

package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 8:04:38 PM To change this template use File | Settings
 * | File Templates.
 */
public class SimpleXMLGraphXPathProvider implements GraphXPathProvider {
  @Override
  public String getNodesPath() {
    return "/graph/systems/system";
  }

  @Override
  public String getEdgesPath() {
    return "/graph/connections/connect";
  }

  @Override
  public String getNodeExtIdPath() {
    return "@id";
  }

  @Override
  public String getNodeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeExtIdPath() {
    return "@id";
  }

  @Override
  public String getEdgeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeSourceExtIdPath() {
    return "@source";
  }

  @Override
  public String getEdgeTargetExtIdPath() {
    return "@target";
  }

  @Override
  public String getEdgeKindPath() {
    return null;
  }

  @Override
  public String getNodeKindPath() {
    return null;
  }
}
