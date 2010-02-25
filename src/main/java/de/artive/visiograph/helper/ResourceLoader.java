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

package de.artive.visiograph.helper;

import de.artive.visiograph.VisioDocument;

import java.io.*;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 24, 2010 Time: 8:04:42 PM To change this template use File | Settings
 * | File Templates.
 */
public abstract class ResourceLoader {

  protected abstract void slurp(Reader resource) throws IOException;

  public void load(String resourceName) throws IOException {
    final File templateFile = new File(resourceName);

    if (templateFile.exists()) {
      (new ResourceHandler<Reader>(templateFile.getPath()) {

        @Override
        protected Reader open() throws IOException {
          return new FileReader(templateFile);
        }

        @Override
        protected void doWithCloseable(Reader resource) throws Exception {
          slurp(resource);
        }

      }).execute();

    } else {
      final InputStream stream = VisioDocument.class.getClassLoader().getResourceAsStream(resourceName);
      if (stream != null) {
        (new ResourceHandler<InputStream>(templateFile.getPath()) {

          @Override
          protected void doWithCloseable(InputStream stream) throws Exception {
            slurp(new InputStreamReader(stream));
          }

        }).execute(stream);
      }
    }
  }

}
