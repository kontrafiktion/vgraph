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

package de.artive.vgraph.graph.loader;

import de.artive.vgraph.*;
import de.artive.vgraph.graph.Edge;
import de.artive.vgraph.graph.Graph;
import de.artive.vgraph.helper.CreateInstance;
import de.artive.vgraph.helper.XmlHelper;
import de.artive.vgraph.main.VGraph;
import nu.xom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

import static de.artive.vgraph.helper.NodesIterator.nodesIterator;

/**
 * Simple loader for XML based models. It uses a {@link GraphXPathProvider} to provide XPath expressions to extract the
 * Nodes, Edges and their attributes.
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
      throw new VGraphException(ErrorCode.XML_GENERIC_PARSING, "error parsing: " + source, e);
    } catch (IOException e) {
      throw new VGraphException(ErrorCode.XML_IO, "error parsing: " + source, e);
    }

  }


  protected void _load(Document document, GraphXPathProvider graphXPathProvider, Graph graph) {

    Nodes xmlNodes = document.query(graphXPathProvider.getNodesPath());
    logger.debug("found {} nodes", xmlNodes.size());
    String nodeExtId = null;
    String nodeText = null;
    for (Iterator<Node> it = nodesIterator(xmlNodes); it.hasNext();) {
      Element xmlNode = (Element) it.next();
      try {
        String path = graphXPathProvider.getNodeExtIdPath();
        nodeExtId = getValue(xmlNode, path, "extId for this node");
        nodeText = getValue(xmlNode, graphXPathProvider.getNodeTextPath(), "text for this node");
        graph.addNode(nodeExtId, nodeText);
        logger.debug("added node: [{}] with text '{}'", nodeExtId, nodeText);
      } catch (VGraphException e) {
        if (VGraph.isStrictParsing()) {
          throw e;
        } else {
          logger.error(e.getMessage());
          logger.error("skipping ...");
          logger.error(xmlNode.toXML());
        }
      }
    }
    Nodes xmlEdges = document.query(graphXPathProvider.getEdgesPath());
    for (Iterator<Node> it = nodesIterator(xmlEdges); it.hasNext();) {
      Element xmlEdge = (Element) it.next();
      try {
        String edgeExtId = getValue(xmlEdge, graphXPathProvider.getEdgeExtIdPath(), "extId for this edge");
        String edgeText = getValue(xmlEdge, graphXPathProvider.getEdgeTextPath(), "text for this edge");
        String edgeSourceExtId = getValue(xmlEdge, graphXPathProvider.getEdgeSourceExtIdPath(), "source-node reference");
        String edgeTargetExtId = getValue(xmlEdge, graphXPathProvider.getEdgeTargetExtIdPath(), "target-node reference");
        de.artive.vgraph.graph.Node sourceNode = graph.getNode(edgeSourceExtId);
        de.artive.vgraph.graph.Node targetNode = graph.getNode(edgeTargetExtId);
        if (sourceNode == null) {
          throw new VGraphException(ErrorCode.LOADER_REFERENCED_NODE_NOT_FOUND,
                                    "could not find connected source node with ID: '" + edgeSourceExtId
                                    + "' referenced by connection: '"
                                    + edgeExtId + "'");
        }
        if (targetNode == null) {
          throw new VGraphException(ErrorCode.LOADER_REFERENCED_NODE_NOT_FOUND,
                                    "could not find connected target node with ID: '" + edgeTargetExtId
                                    + "' referenced by connection: '" + edgeExtId + "'");
        }
        Edge edge = new Edge(edgeExtId, edgeText, sourceNode, targetNode);

        graph.addEdge(edge);
      } catch (VGraphException e) {
        if (VGraph.isStrictParsing()) {
          throw e;
        } else {
          logger.error(e.getMessage());
          logger.error("skipping ...");
          logger.error(xmlEdge.toXML());
        }
      }
    }

    if (0 == graph.getNodes().size()) {
      throw new VGraphException(ErrorCode.LOADER_EMPTY_GRAPH, "Graph loaded is empty. No nodes found in source.");
    }
  }

  private String getValue(Element xmlNode, String path, String nodeDesc) {
    String result = null;
    try {
      result = XmlHelper.getValue(xmlNode, path);
    } catch (XmlNodeNotFoundException e) {

      logger.error("using '{}' there was no '{}' to be found in Element:\n{}",
                   new Object[]{path, nodeDesc, xmlNode.toXML()});
      throw e;
    } catch (XmlTooManyNodesException e) {
      logger.error("using '{}' there were more than one '{}' in Element:\n{}",
                   new Object[]{path, nodeDesc, xmlNode.toXML()});
      throw e;
    }
    return result;
  }

}
