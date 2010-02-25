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

}
