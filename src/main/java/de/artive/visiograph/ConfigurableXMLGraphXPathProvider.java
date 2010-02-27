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

import de.artive.visiograph.helper.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 8:04:38 PM To change this template use File | Settings
 * | File Templates.
 */
public class ConfigurableXMLGraphXPathProvider implements GraphXPathProvider {

  Logger logger = LoggerFactory.getLogger(ConfigurableXMLGraphXPathProvider.class);

  /**
   * (mandatory) references all elements that should be loaded as {@link Node Nodes} into the {@link Graph}
   */
  public static final String NODES_PATH = "nodes";

  /**
   * (mandatory) XPath that references all elements that should be loaded as {@link Edge Edges}
   */
  public static final String EDGES_PATH = "edges";

  /**
   * (mandatory) XPath that reference the node id that uniquely identifies the model element (relative to {@link
   * #NODES_PATH})
   */
  public static final String NODE_EXTID = "node.extId";

  /**
   * (mandatory) references the edge id that uniquely identifies the model element (relative to {@link #EDGES_PATH})
   */
  public static final String EDGE_EXTID = "edge.extId";

  /**
   * (mandatory) references the node id that references the source node of this edge
   */
  public static final String EDGE_SOURCE_EXTID = "edge.source.extId";

  /**
   * (mandatory) reference the node id that references the target node of this edge
   */
  public static final String EDGE_TARGET_EXTID = "edge.target.extId";

  /**
   * references the node text (relative to {@link #NODES_PATH})
   */
  public static final String NODE_TEXT = "node.text";

  /**
   * references the 'kind' of the node (used for mapping to different shapes) (relative to {@link #NODES_PATH})
   */
  public static final String NODE_KIND = "node.kind";

  /**
   * references the edge text (relative to {@link #NODES_PATH})
   */
  public static final String EDGE_TEXT = "edge.text";

  /**
   * references the 'kind' of the edge (used for mapping to different shapes) (relative to {@link #NODES_PATH})
   */
  public static final String EDGE_KIND = "edge.kind";

  public static final String[] MANDATORY_PROPERTIES = new String[]{NODES_PATH, NODE_EXTID, NODE_TEXT, EDGES_PATH, EDGE_EXTID, EDGE_SOURCE_EXTID, EDGE_TARGET_EXTID};

  private Properties properties = new Properties();


  /**
   * Loads XPath expresions from a property file.
   *
   * @param propertiesResourceName
   * @throws IOException
   * @throws VisioGraphException
   */
  public ConfigurableXMLGraphXPathProvider(String propertiesResourceName) throws IOException, VisioGraphException {
    new ResourceLoader() {
      @Override
      protected void slurp(Reader resource) throws IOException {
        properties.load(resource);
        boolean error = false;
        StringBuilder missingProperties = new StringBuilder();
        for (String mandatoryProperty : MANDATORY_PROPERTIES) {
          if (empty(mandatoryProperty)) {
            logger.error("mandatory property '" + mandatoryProperty + "' not found");
            if (error) {
              missingProperties.append(", ");
            }
            missingProperties.append(mandatoryProperty);
            error = true;
          }
        }

        if (error) {
          throw new VisioGraphException("missing mandatory properties: '" + missingProperties + "'");
        }
      }
    }.load(propertiesResourceName);
  }

  private boolean empty(String mandatoryProperty) {
    return properties.get(mandatoryProperty) == null || mandatoryProperty.trim().equals("");
  }

  @Override
  public String getNodesPath() {
    return safeGetPropertyAsString(NODES_PATH);
  }

  @Override
  public String getEdgesPath() {
    return safeGetPropertyAsString(EDGES_PATH);
  }

  @Override
  public String getNodeExtIdPath() {
    return safeGetPropertyAsString(NODE_EXTID);
  }

  @Override
  public String getNodeTextPath() {
    return safeGetPropertyAsString(NODE_TEXT);
  }

  @Override
  public String getNodeKindPath() {
    return safeGetPropertyAsString(NODE_KIND);
  }

  @Override
  public String getEdgeExtIdPath() {
    return safeGetPropertyAsString(EDGE_EXTID);
  }

  @Override
  public String getEdgeTextPath() {
    return safeGetPropertyAsString(EDGE_TEXT);
  }

  @Override
  public String getEdgeSourceExtIdPath() {
    return safeGetPropertyAsString(EDGE_SOURCE_EXTID);
  }

  @Override
  public String getEdgeTargetExtIdPath() {
    return safeGetPropertyAsString(EDGE_TARGET_EXTID);
  }

  @Override
  public String getEdgeKindPath() {
    return safeGetPropertyAsString(EDGE_KIND);
  }

  private String safeGetPropertyAsString(String key) {
    Object property = properties.get(key);
    return (property == null ? null : property.toString());
  }
}