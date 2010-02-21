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

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 13, 2010 Time: 8:04:38 PM To change this template use File | Settings
 * | File Templates.
 */
public class SimpleXMLGraphXPathProvider implements GraphXPathProvider {
  @Override
  public String getNodesPath() {
    return "/graph/systems/system";
  }

  @Override
  public String getEdgesPath() {
    return "/graph/connections/connect";
  }

  @Override
  public String getNodeExtIdPath() {
    return "@id";
  }

  @Override
  public String getNodeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeExtIdPath() {
    return "@id";
  }

  @Override
  public String getEdgeTextPath() {
    return ".";
  }

  @Override
  public String getEdgeSourceExtIdPath() {
    return "@source";
  }

  @Override
  public String getEdgeTargetExtIdPath() {
    return "@target";
  }
}
