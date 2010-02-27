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
