/*
 * Copyright 2010 Victor Volle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
