/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : PACKAGE_NAME
 * @date : 2024-05-15
 * @time : 15:42
 */

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * a
 * <p>
 * Created by jhon on 2024/5/15 15:42
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class TestClassLoader {

    @Test
    public void getSystemResourceAsStream() {
        Properties configProperties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("ssdp.properties")) {
            configProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configProperties.list(System.out);
    }

    @Test
    public void getSystemResource() {//classpath*:
//        String filePathName = ClassLoader.getSystemResource(".").getPath();
        String filePathName = ClassLoader.getSystemResource("/E:/WorkSpace/JhonLee/Java/spring/plugin/ssdp/basic/src/main/resources/ssdp.properties").getPath();
        System.out.println(filePathName);
    }

}
