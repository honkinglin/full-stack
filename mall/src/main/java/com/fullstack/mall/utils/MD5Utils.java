package com.fullstack.mall.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fullstack.mall.common.Constant;

/**
 * MD5加密工具
 */
public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update((strValue + Constant.SALT).getBytes());
        byte[] digest = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
