/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.util
 * @date : 2024-03-06
 * @time : 13:34
 */
package net.lizhaoweb.ssdp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * [工具] 系统
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:34
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
public class SystemUtil {

    /**
     * 操作系统名称
     */
    public static final String OS_NAME = System.getProperty("os.name");

    /**
     * 操作系统架构
     */
    public static final String OS_ARCH = System.getProperty("os.arch");

    /**
     * 操作系统版本
     */
    public static final String OS_VERSION = System.getProperty("os.version");

    /**
     * 获取CPU序列号(Windows)
     *
     * @return
     * @throws IOException
     */
    public static String getCPUSerialNumber() {
        String next;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String serial = sc.next();
            next = sc.next();
        } catch (IOException e) {
            throw new RuntimeException("获取CPU序列号失败");
        }
        return next;
    }

    /**
     * 获取硬盘序列号(Windows)
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getHardDiskSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String serial = sc.next();
            return sc.next();
        } catch (IOException e) {
            throw new RuntimeException("获取硬盘序列号失败");
        }
    }

    /**
     * bois版本号(linux)
     *
     * @return
     */
    public static String getBoisVersion() {
        String result = "";
        Process p;
        try {
            // 管道
            p = Runtime.getRuntime().exec("sudo dmidecode -s bios-version");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
                break;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("获取主板信息错误");
        }
        return result;
    }

    /**
     * 获取系统序列号(linux)
     *
     * @return
     */
    public static String getUUID() {
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
            InputStream in;
            BufferedReader br;
            in = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            while (in.read() != -1) {
                result = br.readLine();
            }
            br.close();
            in.close();
            process.destroy();
            System.out.println("获取序列号：" + result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getSplitString(String str, String split, int length) {
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i % length == 0 && i > 0) {
                temp.append(split);
            }
            temp.append(str.charAt(i));
        }
        return temp.toString();
    }

    /**
     * 获取window or linux机器码
     *
     * @return
     */
    public static String getWindowNumber(String type) {
        if (type == null) {
            return "";
        }
        Map<String, Object> codeMap = new HashMap<>(2);
        String result = "";
        if ("linux".equals(type)) {
            String boisVersion = getBoisVersion();
            codeMap.put("boisVersion", boisVersion);
            System.out.println("boisVersion：" + boisVersion);
            String uuid = getUUID();
            codeMap.put("uuid", uuid);
            System.out.println("uuid：" + uuid);
//            String codeMapStr = JSON.toJSONString(codeMap);
//            String serials = MD5Util.saltingMD5(codeMapStr, SALT);
//            result = getSplitString(serials, "-", 4);
        } else if ("window".equals(type)) {
            String processorId = getCPUSerialNumber();
            codeMap.put("ProcessorId", processorId);
            System.out.println("ProcessorId：" + processorId);
            String serialNumber = getHardDiskSerialNumber();
            codeMap.put("SerialNumber", serialNumber);
            System.out.println("SerialNumber：" + serialNumber);
//            String codeMapStr = JSON.toJSONString(codeMap);
//            String serials = MD5Util.saltingMD5(codeMapStr, SALT);
//            result = getSplitString(serials, "-", 4);
        } else {
            // Nothing
        }
        return result;
    }

}
