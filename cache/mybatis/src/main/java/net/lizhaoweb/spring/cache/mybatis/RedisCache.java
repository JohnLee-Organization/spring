/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.cache.mybatis
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
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
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用第三方内存数据库Redis作为二级缓存
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年05月23日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class RedisCache implements Cache {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    private static final RedisSerializer<Object> DEFAULT_KEY_SERIALIZER = new JdkSerializationRedisSerializer();
    private static final RedisSerializer<Object> DEFAULT_VALUE_SERIALIZER = new GenericJackson2JsonRedisSerializer();

    @Setter
    private RedisSerializer<Object> keySerializer;

    @Setter
    private RedisSerializer<Object> valueSerializer;

    @Getter
    private final String id;

    @Getter
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Setter
    private static JedisConnectionFactory jedisConnectionFactory;

    public RedisCache(String id) {
        if (null == id || "".equals(id.trim())) {
            throw new IllegalArgumentException("Mybatis redis cache need an id.");
        }
        this.id = id;
        LOGGER.debug("Mybatis redis cache id: {}", id);
    }


//    @Override
//    public String getId() {
//        return this.id;
//    }

    public RedisSerializer<Object> getKeySerializer() {
        return keySerializer == null ? DEFAULT_KEY_SERIALIZER : keySerializer;
    }

    public RedisSerializer<Object> getValueSerializer() {
        return valueSerializer == null ? DEFAULT_VALUE_SERIALIZER : valueSerializer;
    }

    /**
     * 存值
     *
     * @param key   Redis 键
     * @param value Redis 值
     */
    @Override
    public void putObject(Object key, Object value) {
        if (null == key) {
            return;
        }
        LOGGER.debug("Mybatis redis cache put. RedisKey={} RedisValue={}", key, value);
        RedisConnection redisConnection = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
//            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
//            redisConnection.set(serializer.serialize(key), serializer.serialize(value));
//
//            // 将key保存到redis.list中
//            redisConnection.lPush(serializer.serialize(id), serializer.serialize(key));


            redisConnection.set(this.getKeySerializer().serialize(key), this.getValueSerializer().serialize(value));

            // 将key保存到redis.list中
            redisConnection.lPush(this.getKeySerializer().serialize(id), this.getValueSerializer().serialize(key));
        } catch (Exception e) {
            LOGGER.error("Mybatis redis cache put exception. RedisKey=" + key + " RedisValue=" + value, e);
        } finally {
//            if (null != redisConnection) {
//                redisConnection.close();
//            }
            this.close(redisConnection);
        }
    }

    /**
     * 取值
     *
     * @param key Redis 键
     * @return Redis 值
     */
    @Override
    public Object getObject(Object key) {
        if (null == key) {
            return null;
        }
        LOGGER.debug("Mybatis redis cache get. RedisKey={}", key);
        RedisConnection redisConnection = null;
        Object result = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
//            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
//            result = serializer.deserialize(redisConnection.get(serializer.serialize(key)));


            result = this.getValueSerializer().deserialize(redisConnection.get(this.getKeySerializer().serialize(key)));
        } catch (Exception e) {
            LOGGER.error("Mybatis redis cache get exception. RedisKey=" + key + " RedisValue=null", e);
        } finally {
//            if (null != redisConnection) {
//                redisConnection.close();
//            }
            this.close(redisConnection);
        }
        return result;
    }

    /**
     * 删值
     *
     * @param key Redis 键
     * @return Redis 值
     */
    @Override
    public Object removeObject(Object key) {
        if (null == key) {
            return null;
        }
        LOGGER.debug("Mybatis redis cache remove. RedisKey={}", key);
        RedisConnection redisConnection = null;
        Object result = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
//            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
//            // 讲key设置为立即过期
//            result = redisConnection.expireAt(serializer.serialize(key), 0);
//
//            // 将key从redis.list中删除
//            redisConnection.lRem(serializer.serialize(id), 0, serializer.serialize(key));


            // 讲key设置为立即过期
            result = redisConnection.expireAt(this.getKeySerializer().serialize(key), 0);

            // 将key从redis.list中删除
            redisConnection.lRem(this.getKeySerializer().serialize(id), 0, this.getValueSerializer().serialize(key));
        } catch (Exception e) {
            LOGGER.error("Mybatis redis cache remove exception. RedisKey=" + key + " RedisValue=" + result, e);
        } finally {
//            if (null != redisConnection) {
//                redisConnection.close();
//            }
            this.close(redisConnection);
        }
        return result;
    }

    /**
     * 清空缓存
     * flushCache="true" 的时候会调用这个地方
     */
    @Override
    public void clear() {
        LOGGER.debug("Mybatis redis cache clear. ");
        RedisConnection redisConnection = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
//            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
//
//            /*
//             * 千万不要直接使用 redisConnection.scriptFlush()、redisConnection.flushDb() 或 redisConnection.flushAll()，
//             * 会把整个redis的东西都清除掉，我不相信你的redis里没有其他东西。获取redis.list中的保存的key值，遍历删除
//             */
//            Long length = redisConnection.lLen(serializer.serialize(id));
//            if (0 == length) {
//                return;
//            }
//            List<byte[]> keyList = redisConnection.lRange(serializer.serialize(id), 0, length - 1);
//            for (byte[] key : keyList) {
//                redisConnection.expireAt(key, 0);
//            }
//            redisConnection.expireAt(serializer.serialize(id), 0);




            /*
             * 千万不要直接使用 redisConnection.scriptFlush()、redisConnection.flushDb() 或 redisConnection.flushAll()，
             * 会把整个redis的东西都清除掉，我不相信你的redis里没有其他东西。获取redis.list中的保存的key值，遍历删除
             */
            Long length = redisConnection.lLen(this.getKeySerializer().serialize(id));
            if (0 == length) {
                return;
            }
            List<byte[]> keyList = redisConnection.lRange(this.getKeySerializer().serialize(id), 0, length - 1);
            for (byte[] key : keyList) {
                redisConnection.expireAt(key, 0);
            }
            redisConnection.expireAt(this.getKeySerializer().serialize(id), 0);
            keyList.clear();
        } catch (Exception e) {
            LOGGER.error("Mybatis redis cache clear exception.", e);
        } finally {
//            if (null != redisConnection) {
//                redisConnection.close();
//            }
            this.close(redisConnection);
        }
    }

    @Override
    public int getSize() {
        int result = 0;
//        try {
//            RedisConnection redisConnection = jedisConnectionFactory.getConnection();
//            long dbSize = redisConnection.dbSize();
//            if ((int) dbSize != dbSize) {
//                throw new ArithmeticException("integer overflow");
//            }
//            result = (int) dbSize;
//        } catch (Exception e) {
//            LOGGER.error("Mybatis redis cache getSize exception. ", e);
//        }


        RedisConnection redisConnection = null;
        try {
            redisConnection = jedisConnectionFactory.getConnection();
            long dbSize = redisConnection.dbSize();
            if ((int) dbSize != dbSize) {
                throw new ArithmeticException("integer overflow");
            }
            result = (int) dbSize;
        } catch (Exception e) {
            LOGGER.error("Mybatis redis cache getSize exception.", e);
        } finally {
            this.close(redisConnection);
        }
        return result;
    }

//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return this.readWriteLock;
//    }

//    /**
//     * 这个地方需要静态注入，这里通过中间类 MybatisRedisCacheTransfer 实现的
//     *
//     * @param jedisConnectionFactory 连接工厂创建基于 JEDIS 的连接。
//     */
//    public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
//        RedisCache.jedisConnectionFactory = jedisConnectionFactory;
//    }

    private void close(RedisConnection redisConnection) {
        if (null != redisConnection) {
            redisConnection.close();
        }
    }
}