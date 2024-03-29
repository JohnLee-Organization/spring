/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.cache.mybatis
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 16:39
 */
package net.lizhaoweb.spring.cache.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用第三方内存数据库Redis作为二级缓存
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年05月23日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class RedisCache implements Cache {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    private static final RedisSerializer<Object> DEFAULT_SERIALIZER = new GenericJackson2JsonRedisSerializer();

    @Getter
    private final String id;

    @Getter
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 需要静态注入，这里通过中间类 MybatisRedisCacheTransfer 实现的
     */
    @Setter
    private static JedisConnectionFactory jedisConnectionFactory;

    public RedisCache(String id) {
        if (null == id || id.trim().length() < 1) {
            throw new IllegalArgumentException("Mybatis redis cache need an id.");
        }
        this.id = id;
        LOGGER.debug("Mybatis redis cache id: {}", id);
    }

    /**
     * 存值
     *
     * @param key   Redis 键
     * @param value Redis 值
     */
    @Override
    public void putObject(final Object key, final Object value) {
        if (null == key) {
            return;
        }
        LOGGER.debug("Mybatis redis cache put. RedisKey={} RedisValue={}", key, value);
        this.execute(new RedisCallback<Void>() {
            @Override
            public Void doWithRedis(RedisConnection connection) {
                connection.set(key.toString().getBytes(), DEFAULT_SERIALIZER.serialize(value));

                // 将key保存到redis.list中
                connection.lPush(id.getBytes(), key.toString().getBytes());
                return null;
            }
        });
    }

    /**
     * 取值
     *
     * @param key Redis 键
     * @return Redis 值
     */
    @Override
    public Object getObject(final Object key) {
        if (null == key) {
            return null;
        }
        LOGGER.debug("Mybatis redis cache get. RedisKey={}", key);
        return this.execute(new RedisCallback<Object>() {
            @Override
            public Object doWithRedis(RedisConnection connection) {
                return DEFAULT_SERIALIZER.deserialize(connection.get(key.toString().getBytes()));
            }
        });
    }

    /**
     * 删值
     *
     * @param key Redis 键
     * @return Redis 值
     */
    @Override
    public Object removeObject(final Object key) {
        if (null == key) {
            return null;
        }
        LOGGER.debug("Mybatis redis cache remove. RedisKey={}", key);
        return this.execute(new RedisCallback<Object>() {
            @Override
            public Object doWithRedis(RedisConnection connection) {
                // 将 key 设置为立即过期
                Object result = connection.expireAt(key.toString().getBytes(), 0);

                // 将 key 从 redis.list 中删除
                connection.lRem(id.getBytes(), 0, key.toString().getBytes());
                return result;
            }
        });
    }

    /**
     * 清空缓存
     * flushCache="true" 的时候会调用这个地方
     */
    @Override
    public void clear() {
        LOGGER.debug("Mybatis redis cache clear.");
        this.execute(new RedisCallback<Void>() {
            @Override
            public Void doWithRedis(RedisConnection connection) {
                /*
                 * 千万不要直接使用 redisConnection.scriptFlush()、redisConnection.flushDb() 或 redisConnection.flushAll()，
                 * 会把整个redis的东西都清除掉，我不相信你的redis里没有其他东西。获取redis.list中的保存的key值，遍历删除
                 */
                Long length = connection.lLen(id.getBytes());
                if (length == null || 0 == length) {
                    return null;
                }
                List<byte[]> keyList = connection.lRange(id.getBytes(), 0, length - 1);
                for (byte[] key : keyList) {
                    connection.expireAt(key, 0);
                }
                connection.expireAt(id.getBytes(), 0);
                return null;
            }
        });
    }

    @Override
    public int getSize() {
        return this.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doWithRedis(RedisConnection connection) {
                int result;
                long dbSize = connection.lLen(id.getBytes());
                if ((int) dbSize != dbSize) {
                    throw new ArithmeticException("integer overflow");
                }
                result = (int) dbSize;
                return result;
            }
        });
    }


    // 执行 Redis 回调
    private <T> T execute(RedisCallback<T> callback) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
            return callback.doWithRedis(redisConnection);
        } finally {
            this.close(redisConnection);
        }
    }

    // 关闭 Redis 连接
    private void close(RedisConnection redisConnection) {
        if (null != redisConnection) {
            redisConnection.close();
        }
    }
}
