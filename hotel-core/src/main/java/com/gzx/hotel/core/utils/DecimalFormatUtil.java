package com.gzx.hotel.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalFormatUtil {

    public static Double format(Double d, Integer digits) {
        BigDecimal bd = new BigDecimal(String.valueOf(d));
        return bd.setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }
}
