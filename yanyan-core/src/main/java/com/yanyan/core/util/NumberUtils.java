package com.yanyan.core.util;

import java.util.Collection;

/**
 * User: Saintcy
 * Date: 2016/12/23
 * Time: 15:55
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    public static boolean equals(Byte n1, Byte n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }

        return n1.byteValue() == n2.byteValue();
    }

    public static boolean equals(Short n1, Short n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }

        return n1.shortValue() == n2.shortValue();
    }

    public static boolean equals(Integer n1, Integer n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }

        return n1.intValue() == n2.intValue();
    }

    public static boolean equals(Long n1, Long n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }

        return n1.longValue() == n2.longValue();
    }

    public static int indexOf(Byte[] array, Byte n) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int i = 0;
        for (Byte a : array) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Short[] array, Short n) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int i = 0;
        for (Short a : array) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Integer[] array, Integer n) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int i = 0;
        for (Integer a : array) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Long[] array, Long n) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int i = 0;
        for (Long a : array) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Collection<Byte> collection, Byte n) {
        if (collection == null || collection.size() == 0) {
            return -1;
        }
        int i = 0;
        for (Byte a : collection) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Collection<Short> collection, Short n) {
        if (collection == null || collection.size() == 0) {
            return -1;
        }
        int i = 0;
        for (Short a : collection) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Collection<Integer> collection, Integer n) {
        if (collection == null || collection.size() == 0) {
            return -1;
        }
        int i = 0;
        for (Integer a : collection) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(Collection<Long> collection, Long n) {
        if (collection == null || collection.size() == 0) {
            return -1;
        }
        int i = 0;
        for (Long a : collection) {
            if (equals(a, n)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static Byte defaultIfEmpty(Byte n) {
        return n == null ? 0 : n;
    }

    public static Byte defaultIfEmpty(Byte n, byte def) {
        return n == null ? def : n;
    }

    public static Short defaultIfEmpty(Short n) {
        return n == null ? 0 : n;
    }

    public static Short defaultIfEmpty(Short n, short def) {
        return n == null ? def : n;
    }

    public static Integer defaultIfEmpty(Integer n) {
        return n == null ? 0 : n;
    }

    public static Integer defaultIfEmpty(Integer n, int def) {
        return n == null ? def : n;
    }

    public static Long defaultIfEmpty(Long n) {
        return n == null ? 0L : n;
    }

    public static Long defaultIfEmpty(Long n, long def) {
        return n == null ? def : n;
    }

    public static Float defaultIfEmpty(Float n) {
        return n == null ? 0.00f : n;
    }

    public static Float defaultIfEmpty(Float n, float def) {
        return n == null ? def : n;
    }

    public static Double defaultIfEmpty(Double n) {
        return n == null ? 0.00d : n;
    }

    public static Double defaultIfEmpty(Double n, double def) {
        return n == null ? def : n;
    }

}
