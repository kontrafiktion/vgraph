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
