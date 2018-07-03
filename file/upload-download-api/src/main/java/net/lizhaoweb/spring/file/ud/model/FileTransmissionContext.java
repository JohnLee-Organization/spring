/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.ud.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:03
 */
package net.lizhaoweb.spring.file.ud.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * <h1>模型 - 文件传输上下文</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月03日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileTransmissionContext {

    private Map<String, List<Object>> context = new ConcurrentHashMap<>();

    // ============================= 基础数据设置 =============================
    public void setByte(String key, byte value) {
        this.setObject(key, value);
    }

    public byte getByte(String key, byte defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setShort(String key, short value) {
        this.setObject(key, value);
    }

    public short getShort(String key, short defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setInt(String key, int value) {
        this.setObject(key, value);
    }

    public int getInt(String key, int defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setLong(String key, long value) {
        this.setObject(key, value);
    }

    public long getLong(String key, long defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        this.setObject(key, value);
    }

    public float getFloat(String key, float defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setDouble(String key, double value) {
        this.setObject(key, value);
    }

    public double getDouble(String key, double defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setChar(String key, char value) {
        this.setObject(key, value);
    }

    public char getChar(String key, char defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        this.setObject(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void set(String key, String value) {
        this.setObject(key, value);
    }

    public String get(String key, String defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setBigDecimal(String key, BigDecimal value) {
        this.setObject(key, value);
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return this.getObject(key, defaultValue);
    }

    public void setPattern(String key, Pattern value) {
        this.setObject(key, value);
    }

    public Pattern getPattern(String key, Pattern defaultValue) {
        return this.getObject(key, defaultValue);
    }

    // ============================= 基础数据增加 =============================
    public void addBytes(String key, byte... values) {
        this.addObjects(key, values);
    }

    public Byte[] getBytes(String key) {
        return this.getObjects(key);
    }

    public void addShorts(String key, short... values) {
        this.addObjects(key, values);
    }

    public Short[] getShorts(String key) {
        return this.getObjects(key);
    }

    public void addInts(String key, int... values) {
        this.addObjects(key, values);
    }

    public Integer[] getInts(String key) {
        return this.getObjects(key);
    }

    public void addLongs(String key, long... values) {
        this.addObjects(key, values);
    }

    public Long[] getLongs(String key) {
        return this.getObjects(key);
    }

    public void addFloats(String key, float... values) {
        this.addObjects(key, values);
    }

    public Float[] getFloats(String key) {
        return this.getObjects(key);
    }

    public void addDoubles(String key, double... values) {
        this.addObjects(key, values);
    }

    public Double[] getDoubles(String key) {
        return this.getObjects(key);
    }

    public void addChars(String key, char... values) {
        this.addObjects(key, values);
    }

    public Character[] getChars(String key) {
        return this.getObjects(key);
    }

    public void addBooleans(String key, boolean... values) {
        this.addObjects(key, values);
    }

    public Boolean[] getBooleans(String key) {
        return this.getObjects(key);
    }

    public void addStrings(String key, String... values) {
        this.addObjects(key, values);
    }

    public String[] getStrings(String key) {
        return this.getObjects(key);
    }

    public void addBigDecimals(String key, BigDecimal... values) {
        this.addObjects(key, values);
    }

    public BigDecimal[] getBigDecimals(String key) {
        return this.getObjects(key);
    }

    public void addPatterns(String key, Pattern... values) {
        this.addObjects(key, values);
    }

    public Pattern[] getPatterns(String key) {
        return this.getObjects(key);
    }


    // ===============================================================================
    private <T> void setObject(String key, T value) {
        List<Object> valueList = context.get(key);
        if (valueList == null) {
            valueList = new ArrayList<>();
        } else {
            valueList.clear();
        }
        valueList.add(value);
        context.put(key, valueList);
    }

    private <T> T getObject(String key, T defaultValue) {
        List<Object> valueList = context.get(key);
        if (valueList == null || valueList.size() < 1) {
            return defaultValue;
        }
        return (T) valueList.get(0);
    }

    private <T> void addObjects(String key, T... values) {
        List<Object> valueList = context.get(key);
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        Collections.addAll(valueList, values);
        context.put(key, valueList);
    }

    private <T> T[] getObjects(String key) {
        List<Object> valueList = context.get(key);
        if (valueList == null) {
            return null;
        }
        return (T[]) valueList.toArray();
    }
}
