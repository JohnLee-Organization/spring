/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.cache.redis
 * @date : 2024-05-29
 * @time : 11:29
 */
package net.lizhaoweb.ssdp.socket.cache.redis;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.socket.service.IMessageCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * a
 * <p>
 * Created by jhon on 2024/5/29 11:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
@Slf4j
public class SsdpRequestCache implements IMessageCache<String, SsdpRequest> {

    private RedisTemplate<String, SsdpRequest> redisTemplate;

    public SsdpRequestCache(RedisTemplate<String, SsdpRequest> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean set(String key, SsdpRequest value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public SsdpRequest get(String key, SsdpRequest defaultValue) {
        SsdpRequest value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
