package br.com.yomigae.cloudstash.core.util;

import static java.lang.Long.parseLong;

public class Utils {
    public static long binaryStringToLong(String string) {
        return parseLong(string.replaceAll(" ", ""), 2);
    }
}
