package com.gzx.hotel.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalFormatUtil {

    public static Double format(Double d) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(3, RoundingMode.HALF_UP).doubleValue();
    }
}
