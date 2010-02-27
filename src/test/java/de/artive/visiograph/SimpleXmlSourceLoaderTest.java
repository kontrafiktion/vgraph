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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * 
 */
public class SimpleXmlSourceLoaderTest {

  private static final ConfigurableXMLGraphXPathProvider XPATH_PROVIDER;

  static {
    try {
      XPATH_PROVIDER = new ConfigurableXMLGraphXPathProvider("SimpleXMLGraph.properties");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void loadSimpleGraph() throws IOException {
    SimpleXmlSourceLoader loader = new SimpleXmlSourceLoader();
    Graph graph = new Graph();
    loader.loadInto("src/test/resources/SimpleXmlSourceLoader/TestSimpleGraph.xml",
                    XPATH_PROVIDER,
                    graph);
    assertEquals(5, graph.getNodes().size());
    assertEquals(4, graph.getEdges().size());

    Edge edge = graph.getEdge("cAB");
    Assert.assertNotNull(edge);
    assertEquals("A", edge.getSource().getExtID());
    assertEquals("B", edge.getTarget().getExtID());
  }

  /**
   * Check that an empty model results in {@link de.artive.visiograph.ErrorCode#LOADER_EMPTY_GRAPH}
   *
   * @throws IOException
   */
  @Test
  public void detectHangingConnector() throws IOException {
    loadWithExpectedErrorCode("src/test/resources/SimpleXmlSourceLoader/TestSimpleGraphHangingConnector.xml",
                              ErrorCode.LOADER_REFERENCED_NODE_NOT_FOUND);
  }

  /**
   * Check that an edge that does not contain a source reference is detected {@link
   * de.artive.visiograph.ErrorCode#XML_NODE_NOT_FOUND}
   *
   * @throws IOException
   */
  @Test
  public void detectUndefinedSource() throws IOException {
    loadWithExpectedErrorCode("src/test/resources/SimpleXmlSourceLoader/TestSimpleGraphUndefinedSource.xml",
                              ErrorCode.XML_NODE_NOT_FOUND);
  }

  /**
   * Check that an empty model results in {@link de.artive.visiograph.ErrorCode#LOADER_EMPTY_GRAPH}
   *
   * @throws IOException
   */
  @Test
  public void detectEmpty() throws IOException {
    loadWithExpectedErrorCode("src/test/resources/SimpleXmlSourceLoader/TestSimpleGraphEmpty.xml",
                              ErrorCode.LOADER_EMPTY_GRAPH);
  }

  private void loadWithExpectedErrorCode(String source, ErrorCode errorCode) {
    SimpleXmlSourceLoader loader = new SimpleXmlSourceLoader();
    Graph graph = new Graph();
    try {
      loader.loadInto(source,
                      XPATH_PROVIDER,
                      graph);
      Assert.fail("expected VisioGraphException with error code: " + errorCode);
    } catch (VisioGraphException e) {
      assertEquals(errorCode, e.getErrorCode());
    }
  }

}
