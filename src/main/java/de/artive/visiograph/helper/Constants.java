package de.artive.visiograph.helper;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 19, 2010 Time: 5:09:44 PM To change this template use File | Settings
 * | File Templates.
 */
public interface Constants {
  BigDecimal ZERO = new BigDecimal("0");
  BigDecimal TWO = new BigDecimal("2");
  BigDecimal INCH = new BigDecimal("25.4");
  BigDecimal VISIO_Y_DIST = new BigDecimal("0.125").multiply(INCH);
}
