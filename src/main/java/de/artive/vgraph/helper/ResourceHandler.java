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

package de.artive.vgraph.helper;

import java.io.*;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 24, 2010 Time: 8:00:15 PM To change this template use File | Settings
 * | File Templates.
 */
public abstract class ResourceHandler<T extends Closeable> {
  String resourceName;

  protected ResourceHandler(String resourceName) {
    this.resourceName = resourceName;
  }

  protected T open() throws IOException {
    throw new IOException("'open' not implemented");
  }

  protected abstract void doWithCloseable(T resource) throws Exception;

  public void execute() throws IOException {
    T resource = open();
    execute(resource);
  }

  public void execute(T resource) throws IOException {
    if (resource != null) {
      IOException exception = null;
      try {
        doWithCloseable(resource);
      } catch (Exception e) {
        exception = new IOException("problem loading: " + resourceName, e);
      } finally {
        try {
          resource.close();
        } catch (IOException e) {
          if (exception == null) {
            exception = new IOException("colud not close: " + resourceName, e);
          } else {
            e.printStackTrace();
          }
        }
        if (exception != null) {
          throw exception;
        }
      }
    } else {
      throw new FileNotFoundException("could not open resource: " + resourceName);
    }

  }


}
