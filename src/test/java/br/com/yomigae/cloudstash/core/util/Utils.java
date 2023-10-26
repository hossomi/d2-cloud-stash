package br.com.yomigae.cloudstash.core.util;

import static java.lang.Byte.parseByte;
import static java.lang.Long.parseLong;

public class Utils {
    public static long binaryToLong(String string) {
        return parseLong(string.replaceAll("[^01]", ""), 2);
    }
    public static byte binaryToByte(String string) {
        return parseByte(string.replaceAll("[^01]", ""), 2);
    }
}
