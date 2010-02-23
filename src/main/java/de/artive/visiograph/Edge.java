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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 1:16:14 PM To change this template use File | Settings
 * | File Templates.
 */
public class Edge extends GraphElement {
  private VisioConnector visioConnector;

  public Edge(String extID, String text, Node source, Node target) {
    super(extID, text);
    this.source = source;
    this.target = target;
  }

  private Node source;

  private Node target;

  private boolean directed;

  private Map<String, String> properties = new HashMap<String, String>();


  public Node getSource() {
    return source;
  }

  public void setSource(Node source) {
    this.source = source;
  }

  public Node getTarget() {
    return target;
  }

  public void setTarget(Node target) {
    this.target = target;
  }

  public boolean isDirected() {
    return directed;
  }

  public void setDirected(boolean directed) {
    this.directed = directed;
  }

  public String getProperty(String key) {
    return properties.get(key);
  }

  public void setProperty(String key, String value) {
    properties.put(key, value);
  }

  public VisioConnector getVisioConnector() {
    return visioConnector;
  }

  public void setVisioConnector(VisioConnector visioConnector) {
    this.visioConnector = visioConnector;
  }

  @Override
  public VisioShape getVisioShape() {
    return getVisioConnector();
  }
}
