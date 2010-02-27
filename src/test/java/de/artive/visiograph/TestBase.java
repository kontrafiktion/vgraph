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

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 24, 2010 Time: 10:32:26 PM To change this template use File | Settings
 * | File Templates.
 */
public class TestBase {

  public void assertCause(String expected, Exception cause) {
    Throwable currentCause = cause;
    boolean found = false;
    while (currentCause != null) {
      if (currentCause.getMessage() != null && currentCause.getMessage().startsWith(expected)) {
        return;
      }
      if (cause.getCause() == null || cause.getCause() == cause) {
        return;
      }
      currentCause = cause.getCause();
    }
    Assert.assertTrue(found, "expected prefix '" + expected + "' could not be found in any message in the stack trace");

  }

}
