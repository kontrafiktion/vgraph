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

package de.artive.visiograph;

import de.artive.visiograph.helper.CreateInstance;
import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;
import nu.xom.Node;

import java.io.IOException;
import java.util.Iterator;

import static de.artive.visiograph.helper.NodesIterator.nodesIterator;

/**
 * Simple loader for XML based models. It uses a {@link de.artive.visiograph.GraphXPathProvider} to provide XPath
 * expressions to extract the Nodes, Edges and their attributes.
 * <p/>
 * User: vivo Date: Feb 13, 2010 Time: 7:09:31 PM
 */
public class SimpleXmlSourceLoader implements SourceLoader {

  /**
   *
   */
  public SimpleXmlSourceLoader() {
  }


  @Override
  public void load(String sourceName, Graph graph, String configuration) {
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
      throw new VisioGraphException("error parsing: " + source, e);
    } catch (IOException e) {
      throw new VisioGraphException("error parsing: " + source, e);
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
        throw new VisioGraphException(
            "could not find connect node with ID: " + edgeTargetExtId + " referenced by connection: " + edgeExtId);
      }
      Edge edge = new Edge(edgeExtId, edgeText, sourceNode, targetNode);

      graph.addEdge(edge);
    }
  }

}
