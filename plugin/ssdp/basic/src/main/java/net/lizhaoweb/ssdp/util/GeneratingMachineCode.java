/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.util
 * @date : 2024-03-13
 * @time : 11:42
 */
package net.lizhaoweb.ssdp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 生成机器码
 * <p>
 * Created by Jhon.Lee on 2024/3/13 11:42
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class GeneratingMachineCode {

    private static Logger log = LoggerFactory.getLogger(GeneratingMachineCode.class);

    /**
     * 获取CPU序列号(Windows)
     *
     * @return
     * @throws
     */
    public static String getCPUSerialNumber() {
        String serial = "";
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            while (sc.hasNext()) {
                serial = sc.next();
                if (!"ProcessorId".equalsIgnoreCase(serial)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("获取CPU序列号失败");
        }
        return serial;
    }

    /**
     * 获取 硬盘序列号(Windows)
     *
     * @return
     * @throws
     * @throws InterruptedException
     */
    public static String getHardDiskSerialNumber() {
        String serial = "";
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            while (sc.hasNext()) {
                serial = sc.next();
                if (!"SerialNumber".equalsIgnoreCase(serial)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("获取硬盘序列号失败");
        }
        return serial;
    }

    /**
     * 注：liunx上 如果想获取的话  需要root用户来执行 ；如果使用普通用户 执行的话  需要输入当前用户的密码（普通用户不支持dmidecode命令 因为没权限）
     */

    /**
     * bois版本号(linux)
     *
     * @return
     */
    public static String getBoisVersion() {
        String result = "";
        Process p;
        try {
            p = Runtime.getRuntime().exec("sudo dmidecode -s bios-version");// 管道
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
                break;
            }
            br.close();
        } catch (IOException e) {
            log.error("获取主板信息错误", e);
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
            // log.info("获取序列号：{}",result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 生成序列号的main方法
     *
     * @param args
     */
    public static void main(String[] args) {
        String property = System.getProperty("os.name").toLowerCase();
        String cpuSerialNumber = "";
        String hardDiskSerialNumber = "";
        String md5Result = "";
        if (property.contains("windows")) {
            // 获取cpu序列号
            cpuSerialNumber = GeneratingMachineCode.getCPUSerialNumber();
            // 获取 硬盘号
            hardDiskSerialNumber = GeneratingMachineCode.getHardDiskSerialNumber();
        } else if (property.contains("linux")) {
            // 获取cpu序列号
            cpuSerialNumber = GeneratingMachineCode.getUUID();
            // 获取 硬盘号
            hardDiskSerialNumber = GeneratingMachineCode.getBoisVersion();
        }
        // 获取到cpu序列号和硬盘号
        log.info("key:{}{}", cpuSerialNumber, hardDiskSerialNumber);
    }
}
