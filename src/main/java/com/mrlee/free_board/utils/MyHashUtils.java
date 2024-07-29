package com.mrlee.free_board.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHashUtils {

    public static String convertToSHA256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] input = str.getBytes();
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("해당하는 알고리즘을 찾을 수 없습니다.");
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
