package com.pixeltects.core.utils.math;

import java.text.NumberFormat;

public class MathArgUtils {

    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatNumber(double number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatNumber(float number) {
        return NumberFormat.getInstance().format(number);
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
