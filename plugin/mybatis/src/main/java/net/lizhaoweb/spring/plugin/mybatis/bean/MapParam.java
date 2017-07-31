/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.plugin.mybatis.bean
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @Time : 10:47
 */
package net.lizhaoweb.spring.plugin.mybatis.bean;

import java.util.HashMap;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @notes Created on 2016年07月19日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class MapParam extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 作为Key的字段对应MapParam的Key
     */
    public static final String KEY_FIELD = "mapKeyField";
    /**
     * 作为Value的字段对应MapParam的Key
     */
    public static final String VALUE_FIELD = "mapValueField";

    public MapParam() {

    }

    /**
     * 指定keyField和valueField
     *
     * @param keyField   Map中key对应的字段
     * @param valueField Map中value对应的字段
     */
    public MapParam(String keyField, String valueField) {
        this.put(KEY_FIELD, keyField);
        this.put(VALUE_FIELD, valueField);
    }
}
