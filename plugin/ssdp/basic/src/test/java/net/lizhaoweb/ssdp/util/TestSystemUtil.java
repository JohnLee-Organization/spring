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
public class TestSystemUtil {

    @Test
    public void cpuSerialNumber() {
        System.out.println("CPU SerNo.: " + SystemUtil.getCPUSerialNumber());
    }

    @Test
    public void hardDiskSerialNumber() {
        System.out.println("Disk SerNo.: " + SystemUtil.getHardDiskSerialNumber());
    }

    @Test
    public void boisVersion() {
        System.out.println("Bois Version: " + SystemUtil.getBoisVersion());
    }

    @Test
    public void UUID() {
        System.out.println("UUID: " + SystemUtil.getUUID());
    }

    @Test
    public void machineNumber() {
        System.out.println("Machine No.: " + SystemUtil.getMachineNumber());
    }

    @Test
    public void machineNumber2() {
        System.out.println("Machine No. 2: " + SystemUtil.getMachineNumber("window"));
    }
}
