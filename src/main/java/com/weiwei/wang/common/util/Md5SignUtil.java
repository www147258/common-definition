package com.weiwei.wang.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Md5SignUtil {

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    private Md5SignUtil() {
    }

    public static String md5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return toHex(digest.digest(content.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("MD5 algorithm not found", exception);
        }
    }

    public static String sign(String content, String secret) {
        return md5(content + secret);
    }

    public static String sign(Map<String, ?> params, String secret) {
        String content = canonicalize(params);
        return sign(content, secret);
    }

    public static boolean verify(String content, String secret, String signature) {
        return Objects.equals(sign(content, secret), signature);
    }

    public static boolean verify(Map<String, ?> params, String secret, String signature) {
        return Objects.equals(sign(params, secret), signature);
    }

    public static String canonicalize(Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        return params.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> !"sign".equalsIgnoreCase(entry.getKey()))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private static String toHex(byte[] bytes) {
        char[] result = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            result[i * 2] = HEX[value >>> 4];
            result[i * 2 + 1] = HEX[value & 0x0F];
        }
        return new String(result);
    }
}
