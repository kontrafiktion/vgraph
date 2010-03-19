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

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 19, 2010 Time: 5:09:44 PM To change this template use File | Settings
 * | File Templates.
 */
public final class VisioHelper {
  public static final BigDecimal ZERO = new BigDecimal("0");
  public static final BigDecimal TWO = new BigDecimal("2");
  public static final BigDecimal INCH = new BigDecimal("25.4");
  public static final BigDecimal VISIO_Y_DIST = new BigDecimal("0.125");
  public static final BigDecimal VISIO_Y_DIST_MM = VISIO_Y_DIST.multiply(INCH);
  public static final BigDecimal VISIO_MINIMAL_SIZE = new BigDecimal("0.25");

  public static final BigDecimal divideByTwo(BigDecimal number) {
    return number.divide(TWO, MathContext.DECIMAL32);
  }

  public static final BigDecimal inch2MM(BigDecimal value) {
    return value.multiply(INCH);
  }

  public static final BigDecimal mm2Inch(BigDecimal value) {
    return value.divide(INCH, MathContext.DECIMAL32);
  }

  public static final BigDecimal abs(BigDecimal value) {
    return value.abs(MathContext.DECIMAL32);
  }

}
