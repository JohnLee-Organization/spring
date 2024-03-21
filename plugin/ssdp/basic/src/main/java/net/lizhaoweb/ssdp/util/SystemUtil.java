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

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

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

    private static Logger log = LoggerFactory.getLogger(SystemUtil.class);

    private static String SALT = "John.Lee";

    /**
     * 获取CPU序列号(Windows)
     *
     * @return String
     */
    public static String getCPUSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner scanner = new Scanner(process.getInputStream());
            String serial = scanner.next();
            if (scanner.hasNext()) {
                serial = scanner.next();
            }
            return serial;
        } catch (Throwable e) {
            throw new RuntimeException("获取CPU序列号失败", e);
        }
    }

    /**
     * 获取硬盘序列号(Windows)
     *
     * @return String
     */
    public static String getHardDiskSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
            process.getOutputStream().close();
            Scanner scanner = new Scanner(process.getInputStream());
            String serial = scanner.next();
            if (scanner.hasNext()) {
                serial = scanner.next();
            }
            return serial;
        } catch (Throwable e) {
            throw new RuntimeException("获取硬盘序列号失败", e);
        }
    }

    /**
     * bois版本号(linux)
     *
     * @return String
     */
    public static String getBoisVersion() {
        try {
            // 管道
            Process process = Runtime.getRuntime().exec("sudo dmidecode -s bios-version");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String version = "";
//            String line;
//            while ((line = br.readLine()) != null) {
//                version += line;
//                break;
//            }
//            bufferedReader.close();
            String line = bufferedReader.readLine();
            bufferedReader.close();
            if (line != null) {
                version += line;
            }
            return version;
        } catch (Throwable e) {
            throw new RuntimeException("获取主板信息错误", e);
        }
    }

    /**
     * 获取系统序列号(linux)
     *
     * @return String
     */
    public static String getUUID() {
        try {
            Process process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
            InputStream inputStream;
            BufferedReader bufferedReader;
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String systemUUID = "";
            while (inputStream.read() != -1) {
                systemUUID = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStream.close();
            process.destroy();
            return systemUUID;
        } catch (Throwable e) {
            throw new RuntimeException("获取系统序列号", e);
        }
    }

    /**
     * 获取window or linux机器码
     *
     * @param type 操作系统类型
     * @return String
     */
    public static String getMachineNumber(String type) {
        if (type == null) {
            return "";
        }
        Map<String, Object> codeMap = new HashMap<>(2);
        String result = "";
        if ("linux".equals(type)) {
            String boisVersion = getBoisVersion();
            codeMap.put("boisVersion", boisVersion);
            log.info("boisVersion：" + boisVersion);
            String uuid = getUUID();
            codeMap.put("uuid", uuid);
            log.info("uuid：" + uuid);
            String codeMapStr = JSON.toJSONString(codeMap);
            String serials = MD5Utils.saltingMD5(codeMapStr, SALT);
            result = getSplitString(serials, "-", 4);
        } else if ("window".equals(type)) {
            String processorId = getCPUSerialNumber();
            codeMap.put("ProcessorId", processorId);
            log.info("ProcessorId：" + processorId);
            String serialNumber = getHardDiskSerialNumber();
            codeMap.put("SerialNumber", serialNumber);
            log.info("SerialNumber：" + serialNumber);
            String codeMapStr = JSON.toJSONString(codeMap);
            String serials = MD5Utils.saltingMD5(codeMapStr, SALT);
            result = getSplitString(serials, "-", 4);
//        } else {
            // Nothing
        }
        return result;
    }

    /**
     * 获取window or linux机器码
     *
     * @return String
     */
    public static String getMachineNumber() {
        Map<String, Object> codeMap = new HashMap<>(2);
        String result = "";
        if (IS_OS_WINDOWS) {
            String processorId = getCPUSerialNumber();
            codeMap.put("ProcessorId", processorId);
            log.info("ProcessorId：" + processorId);
            String serialNumber = getHardDiskSerialNumber();
            codeMap.put("SerialNumber", serialNumber);
            log.info("SerialNumber：" + serialNumber);
            String codeMapStr = JSON.toJSONString(codeMap);
            String serials = MD5Utils.saltingMD5(codeMapStr, SALT);
            result = getSplitString(serials, "-", 4);
        } else if (IS_OS_LINUX) {
            String boisVersion = getBoisVersion();
            codeMap.put("boisVersion", boisVersion);
            log.info("boisVersion：" + boisVersion);
            String uuid = getUUID();
            codeMap.put("uuid", uuid);
            log.info("uuid：" + uuid);
            String codeMapStr = JSON.toJSONString(codeMap);
            String serials = MD5Utils.saltingMD5(codeMapStr, SALT);
            result = getSplitString(serials, "-", 4);
//        } else if (IS_OS_UNIX) {
            // Nothing
//        } else {
            // Nothing
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

}
