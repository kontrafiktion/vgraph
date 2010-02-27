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

import org.testng.annotations.Test;

import java.io.IOException;

/**
 *
 */
public class LargeGraphTest {

  private static final ConfigurableXMLGraphXPathProvider XPATH_PROVIDER;

  static {
    try {
      XPATH_PROVIDER = new ConfigurableXMLGraphXPathProvider("SimpleXMLGraph.properties");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void loadLargeGraph() throws IOException {
    VisioGraph.merge("src/test/resources/Layout/LayoutLargeGraph.xml", "large.vdx");
  }


}