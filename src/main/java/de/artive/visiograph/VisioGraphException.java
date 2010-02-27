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
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 8:46:05 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioGraphException extends RuntimeException {
  private ErrorCode errorCode;


  public VisioGraphException(ErrorCode errorCode, String message) {
    super("[" + errorCode + "] " + message);
    this.errorCode = errorCode;
  }

  public VisioGraphException(ErrorCode errorCode, String message, Throwable cause) {
    super("[" + errorCode + "] " + message, cause);
    this.errorCode = errorCode;
  }

  // FIXME: remove

  public VisioGraphException(String message) {
    super(message);
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
