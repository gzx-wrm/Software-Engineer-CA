package com.gzx.hotel.core.utils;

import java.util.Base64;
import java.util.Random;

/**
 * 通用的工具类
 */
public class CommonUtil {

    private static final Random random = new Random();

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomNum(int digit) {
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    public static String randomSeq(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char c = CHARACTERS.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String b64Decode(String text) {
        return new String(Base64.getDecoder().decode(text));
    }

    public static void main(String[] args) {
        System.out.println("Decoded string: " + CommonUtil.b64Decode("SGVsbG8sIFdvcmxkIQ=="));
    }
}
