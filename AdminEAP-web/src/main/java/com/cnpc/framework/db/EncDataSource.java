package com.cnpc.framework.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncDataSource extends DruidDataSource {
    private final static String KEY_TXT = "n_N6,uo!tZnvbygu";
    private static Logger logger = LoggerFactory.getLogger(EncDataSource.class);

    @Override
    public void setPassword(String password) {
        String dec = decryptAES(password, KEY_TXT);
        super.setPassword(dec);
    }

    @Override
    public void setUsername(String username) {
        String dec = decryptAES(username, KEY_TXT);
        super.setUsername(dec);
    }

    public static String encryptAES(String encryptedHexTxt, String keyTxt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encryptedBytes = encryptedHexTxt.getBytes("utf-8");
            byte[] original = cipher.doFinal(encryptedBytes);
            return bytesToHexString(original);
        } catch (Exception ex) {
            logger.error("ERROR: decrypt failed. error=" + ex.getMessage());
            return null;
        }
    }

    public static String decryptAES(String encryptedHexTxt, String keyTxt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] encryptedBytes = hexStringToBytes(encryptedHexTxt);
            byte[] original = cipher.doFinal(encryptedBytes);
            return new String(original);
        } catch (Exception ex) {
            logger.error("ERROR: decrypt failed. error=" + ex.getMessage());
            return null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] a) {
        System.out.println(encryptAES("root", KEY_TXT));
        System.out.println(encryptAES("msqlro0t", KEY_TXT));
    }
}
