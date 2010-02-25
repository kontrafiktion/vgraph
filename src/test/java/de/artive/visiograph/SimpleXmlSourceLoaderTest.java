/*
 * Copyright (c) 2010, Victor Volle
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of the visiograph nor the names
 *       of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written
 *       permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
