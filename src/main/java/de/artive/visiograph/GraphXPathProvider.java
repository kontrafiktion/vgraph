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
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 6:57:06 PM To change this template use File | Settings
 * | File Templates.
 */
public interface GraphXPathProvider {

  String getNodesPath();

  String getEdgesPath();

  String getNodeExtIdPath();

  String getNodeTextPath();

  String getEdgeExtIdPath();

  String getEdgeTextPath();

  String getEdgeSourceExtIdPath();

  String getEdgeTargetExtIdPath();

  String getEdgeKindPath();

  String getNodeKindPath();

}
