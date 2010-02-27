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


/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 24, 2010 Time: 9:41:05 PM To change this template use File | Settings
 * | File Templates.
 */
public class ConfigurableXMLGraphXPathProviderTest {

  @Test(expectedExceptions = IOException.class)
  public void testMissingMandatory() throws IOException {

    ConfigurableXMLGraphXPathProvider xpathProvider = new ConfigurableXMLGraphXPathProvider(
        "ConfigurableXMLGraphXPathProvider/missingNodeExtId.properties");
  }

  @Test
  public void testSimpleGraph() throws IOException {

    ConfigurableXMLGraphXPathProvider xpathProvider = new ConfigurableXMLGraphXPathProvider(
        "SimpleXMLGraph.properties");

    Assert.assertEquals("/graph/systems/system", xpathProvider.getNodesPath());
    Assert.assertEquals("/graph/connections/connect", xpathProvider.getEdgesPath());
    Assert.assertEquals("@id", xpathProvider.getNodeExtIdPath());
    Assert.assertEquals("@id", xpathProvider.getEdgeExtIdPath());
    Assert.assertEquals(".", xpathProvider.getNodeTextPath());
    Assert.assertEquals(".", xpathProvider.getEdgeTextPath());
    Assert.assertEquals("@source", xpathProvider.getEdgeSourceExtIdPath());
    Assert.assertEquals("@target", xpathProvider.getEdgeTargetExtIdPath());
    Assert.assertEquals("@target", xpathProvider.getEdgeTargetExtIdPath());
    Assert.assertNull(xpathProvider.getNodeKindPath());
    Assert.assertNull(xpathProvider.getEdgeKindPath());

  }


}
