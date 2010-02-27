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

/**
 * A very genric interface. IMplementation must be able to load a {@link de.artive.visiograph.Graph}
 */
public interface GraphLoader {


  /**
   * Loads the <code>graph</code> from the given <code>source</code>.
   * <p/>
   * How the <code>source</code> and the <code>configuration</code> are interpreted is completely up to the
   * implementation (see e.g. {@link de.artive.visiograph.SimpleXmlSourceLoader} and {@link
   * de.artive.visiograph.ConfigurableXMLGraphXPathProvider}).
   * <p/>
   * Since 'visiograph' is normally invked by the command line, all configuration must be possible using String
   *
   * @param source        the source from which the graph shall be loaded (this might be a file name, a table name in a
   *                      DB, etc.. The interpetation is up to the implementation
   * @param configuration an (optional) configuration, again this is completely up to the implementation
   * @param graph         the graph into which the source shall be loaded
   * @throws VisioGraphException (see {@link de.artive.visiograph.ErrorCode} for possible errors)
   */
  public void load(String source, String configuration, Graph graph) throws VisioGraphException;
}
