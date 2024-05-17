/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service.impl
 * @date : 2024-05-15
 * @time : 11:28
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.config.SsdpConfig;
import net.lizhaoweb.ssdp.exception.SsdpIOException;
import net.lizhaoweb.ssdp.service.ISsdpConfigurationService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * [实现] SSDP配置服务
 * <p>
 * Created by jhon on 2024/5/15 11:28
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
@Slf4j
public class SsdpConfigurationService implements ISsdpConfigurationService {


    @Override
    public SsdpConfig loadConfig(String... configFiles) {
        Properties configProperties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("ssdp.properties")) {
            configProperties.load(inputStream);
        } catch (IOException e) {
            throw new SsdpIOException(e);
        }
        if (ArrayUtils.isEmpty(configFiles)) {
            SsdpConfig ssdpConfiguration = new SsdpConfig();
            ssdpConfiguration.convert(configProperties);
            return ssdpConfiguration;
        }
        for (String configFile : configFiles) {
            if (StringUtils.isBlank(configFile)) {
                continue;
            }
            //TODO 缺少
//            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(configFile)) {
//                configProperties.load(inputStream);
//            } catch (IOException e) {
//                throw new SsdpIOException(e);
//            }
        }
        return null;
    }
}
