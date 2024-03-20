/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.util
 * @date : 2024-03-20
 * @time : 10:31
 */
package net.lizhaoweb.ssdp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 10:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class MD5Utils {

    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);

    /*
     * MD5加密
     *
     * @param plaintext 要进行MD5加密的字符串
     * @return 加密结果为byte数组
     */
    private static byte[] md5(String plaintext) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(plaintext.getBytes(UTF_8));
            return algorithm.digest();
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    /*
     * byte数组转为16进制字符串
     *
     * @param byteArray byte数组
     * @return 结果为16进制字符串
     */
    private static String toHex(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        StringBuilder md5StrBuff = new StringBuilder(byteArray.length * 2);
        for (byte _byte : byteArray) {
            if ((_byte & 0xFF) < 0x10) {
                md5StrBuff.append("0");
            }
            md5StrBuff.append(Long.toString(_byte & 0xFF, 16));
        }
        return md5StrBuff.toString();
    }

    /**
     * MD5加密
     *
     * @param plaintext 要进行MD5加密的字符串
     * @return 加密结果为32位字符串
     */
    public static String MD5(String plaintext) {
        try {
            return new String(Objects.requireNonNull(toHex(md5(plaintext))).getBytes(UTF_8), UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...", e);
            return plaintext;
        }
    }

//    /**
//     * MD5加密
//     *
//     * @param message 要进行MD5加密的字符串
//     * @return 加密结果为32位字符串
//     */
//    private static String getMD5(String message) {
//        MessageDigest messageDigest = null;
//        StringBuilder md5StrBuff = new StringBuilder();
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.reset();
//            messageDigest.update(message.getBytes(UTF_8));
//            byte[] byteArray = messageDigest.digest();
//            for (byte _byte : byteArray) {
//                if (Integer.toHexString(0xFF & _byte).length() == 1) {
//                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & _byte));
//                } else {
//                    md5StrBuff.append(Integer.toHexString(0xFF & _byte));
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//        return md5StrBuff.toString().toUpperCase();
//    }

    /**
     * Md5加盐加密
     *
     * @param plaintext 要进行MD5加密的字符串
     * @param salt      加盐
     * @return 加密结果为32位字符串
     */
    public static String saltingMD5(String plaintext, String salt) {
        return MD5(MD5(plaintext) + MD5(salt));
    }

}
