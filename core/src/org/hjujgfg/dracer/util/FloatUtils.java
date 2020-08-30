package org.hjujgfg.dracer.util;

public class FloatUtils {

    public static boolean bigger(float f1, float f2) {
        return bigger(f1, f2, 0.01f);
    }

    public static boolean bigger(float f1, float f2, float precision) {
        return f1 - f2 >= precision;
    }

    public static boolean someWhatSimilar(float f1, float f2, float precision) {
        return Math.abs(f1 - f2) < precision;
    }
}
