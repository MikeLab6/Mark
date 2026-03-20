package com.example.util;

import cn.hutool.crypto.digest.DigestUtil;

public class MD5Util {

    public static String encrypt(String source) {
        return DigestUtil.md5Hex(source);
    }

    public static boolean verify(String source, String encrypted) {
        return encrypt(source).equals(encrypted);
    }
}
