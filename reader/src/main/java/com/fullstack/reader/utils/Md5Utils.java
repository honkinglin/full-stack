package com.fullstack.reader.utils;

import org.springframework.util.DigestUtils;

public class Md5Utils {
    public static String md5(String source, Integer salt) {
        char[] ca = source.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
        return DigestUtils.md5DigestAsHex(target.getBytes());
    }
}
