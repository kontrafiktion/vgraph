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
