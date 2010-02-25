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