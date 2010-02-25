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
