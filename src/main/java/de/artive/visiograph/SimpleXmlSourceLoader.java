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

import de.artive.visiograph.helper.CreateInstance;
import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;
import nu.xom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

import static de.artive.visiograph.helper.NodesIterator.nodesIterator;

/**
 * Simple loader for XML based models. It uses a {@link de.artive.visiograph.GraphXPathProvider} to provide XPath
 * expressions to extract the Nodes, Edges and their attributes.
 * <p/>
 * User: vivo Date: Feb 13, 2010 Time: 7:09:31 PM
 */
public class SimpleXmlSourceLoader implements GraphLoader {
  Logger logger = LoggerFactory.getLogger(SimpleXmlSourceLoader.class);


  /**
   *
   */
  public SimpleXmlSourceLoader() {
  }



  @Override
  public void load(String sourceName, String configuration, Graph graph) {
    GraphXPathProvider graphXPathProvider = null;
    if (configuration != null) {
      graphXPathProvider = CreateInstance.createInstance(configuration);
    } else {
      graphXPathProvider = new SimpleXMLGraphXPathProvider();
    }
    loadInto(sourceName, graphXPathProvider, graph);
  }

  public void loadInto(String source, GraphXPathProvider graphXPathProvider, Graph graph) {
    try {
      Document document = new Builder().build(source);
      _load(document, graphXPathProvider, graph);
    } catch (ParsingException e) {
      throw new VisioGraphException(ErrorCode.XML_GENERIC_PARSING, "error parsing: " + source, e);
    } catch (IOException e) {
      throw new VisioGraphException(ErrorCode.XML_IO, "error parsing: " + source, e);
    }

  }


  protected void _load(Document document, GraphXPathProvider graphXPathProvider, Graph graph) {

    Nodes xmlNodes = document.query(graphXPathProvider.getNodesPath());
    for (Iterator<Node> it = nodesIterator(xmlNodes); it.hasNext();) {
      Element xmlNode = (Element) it.next();
      String nodeExtId = XmlHelper.getValue(xmlNode, graphXPathProvider.getNodeExtIdPath());
      String nodeText = XmlHelper.getValue(xmlNode, graphXPathProvider.getNodeTextPath());
      graph.addNode(nodeExtId, nodeText);
    }
    Nodes xmlEdges = document.query(graphXPathProvider.getEdgesPath());
    for (Iterator<Node> it = nodesIterator(xmlEdges); it.hasNext();) {
      Element xmlEdge = (Element) it.next();
      String edgeExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeExtIdPath());
      String edgeText = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeTextPath());
      String edgeSourceExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeSourceExtIdPath());
      String edgeTargetExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeTargetExtIdPath());
      de.artive.visiograph.Node sourceNode = graph.getNode(edgeSourceExtId);
      de.artive.visiograph.Node targetNode = graph.getNode(edgeTargetExtId);
      if (sourceNode == null) {
        throw new VisioGraphException(
            "could not find connect node with ID: " + edgeSourceExtId + " referenced by connection: " + edgeExtId);
      }
      if (targetNode == null) {
        throw new VisioGraphException(ErrorCode.LOADER_REFERENCED_NODE_NOT_FOUND,
                                      "could not find connect node with ID: " + edgeTargetExtId
                                      + " referenced by connection: " + edgeExtId);
      }
      Edge edge = new Edge(edgeExtId, edgeText, sourceNode, targetNode);

      graph.addEdge(edge);
    }

    if (0 == graph.getNodes().size()) {
      throw new VisioGraphException(ErrorCode.LOADER_EMPTY_GRAPH, "Graph loaded is empty. No nodes found in source.");
    }
  }

}
