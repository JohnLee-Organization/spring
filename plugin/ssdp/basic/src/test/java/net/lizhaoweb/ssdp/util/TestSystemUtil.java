/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.util
 * @date : 2024-03-20
 * @time : 11:27
 */
package net.lizhaoweb.ssdp.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 11:27
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class TestSystemUtil {

    @BeforeClass
    public static void setUpBeforeClass() {
        // 设置系统属性，指定logback配置文件位置
        System.setProperty("logback.configurationFile", "F:\\WorkSpace\\JhonLee\\Java\\spring\\plugin\\ssdp\\basic\\src\\test\\resources\\logback.xml");
    }

    @Test
    public void cpuSerialNumber() {
        log.info("CPU SerNo.: " + SystemUtil.getCPUSerialNumber());
    }

    @Test
    public void hardDiskSerialNumber() {
        log.info("Disk SerNo.: " + SystemUtil.getHardDiskSerialNumber());
    }

    @Test
    public void boisVersion() {
        log.info("Bois Version: " + SystemUtil.getBoisVersion());
    }

    @Test
    public void UUID() {
        log.info("UUID: " + SystemUtil.getUUID());
    }

    @Test
    public void machineNumber() {
        log.info("Machine No.: " + SystemUtil.getMachineNumber());
    }

    @Test
    public void machineNumber2() {
        log.info("Machine No. 2: " + SystemUtil.getMachineNumber("window"));
    }
}
