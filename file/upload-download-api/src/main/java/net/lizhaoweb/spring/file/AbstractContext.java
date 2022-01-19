/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 16:05
 */
package net.lizhaoweb.spring.file;

import net.lizhaoweb.spring.file.exception.NullPointerForFieldNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2019年08月13日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractContext<Config extends AbstractConfig> {

    private Map<String, List> fieldMap = new ConcurrentHashMap<>();

    public AbstractContext(Config config) {
        this.setField("", config);
    }

    public <T> void setField(String fieldName, T fieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = new ArrayList();
        valueList.add(fieldValue);
        fieldMap.put(fieldName, valueList);
    }

    public <T> void addField(String fieldName, T fieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            valueList = new ArrayList();
        }
        valueList.add(fieldValue);
        fieldMap.put(fieldName, valueList);
    }

//    public <T> T[] getFields(String fieldName, T[] defaultFieldValues) {
//        if (fieldName == null) {
//            throw new NullPointerForFieldNameException("The name of the field is null.");
//        }
//        List valueList=fieldMap.get(fieldName);
//        if (valueList==null){
//            return defaultFieldValues;
//        }
//        return valueList.toArray();
//    }

    public Object[] getFields(String fieldName, Object[] defaultFieldValues) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValues;
        }
        return valueList.toArray();
    }

    public String[] getFields(String fieldName, String[] defaultFieldValues) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValues;
        }
        return (String[]) valueList.toArray(new String[0]);
    }

    public String getStringField(String fieldName, String defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return valueList.get(0).toString();
    }

    public boolean getBooleanField(String fieldName, boolean defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return false;
        }
        return Boolean.valueOf(valueList.get(0).toString());
    }

    public Boolean getBooleanField(String fieldName, Boolean defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return Boolean.valueOf(valueList.get(0).toString());
    }

    public byte getByteField(String fieldName, byte defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return 0;
        }
        return Byte.valueOf(valueList.get(0).toString());
    }

    public Byte getByteField(String fieldName, Byte defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return Byte.valueOf(valueList.get(0).toString());
    }

    public short getShortField(String fieldName, short defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return 0;
        }
        return Short.valueOf(valueList.get(0).toString());
    }

    public Short getStringField(String fieldName, Short defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return Short.valueOf(valueList.get(0).toString());
    }

    public int getIntField(String fieldName, int defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return 0;
        }
        return Integer.valueOf(valueList.get(0).toString());
    }

    public Integer getStringField(String fieldName, Integer defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return Integer.valueOf(valueList.get(0).toString());
    }

    public long getLongField(String fieldName, long defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return 0;
        }
        return Long.valueOf(valueList.get(0).toString());
    }

    public Long getStringField(String fieldName, Long defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return Long.valueOf(valueList.get(0).toString());
    }

    public Class getClassField(String fieldName, Class defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return (Class) valueList.get(0);
    }

    public Pattern getPatternField(String fieldName, Pattern defaultFieldValue) {
        if (fieldName == null) {
            throw new NullPointerForFieldNameException("The name of the field is null.");
        }
        List valueList = fieldMap.get(fieldName);
        if (valueList == null) {
            return defaultFieldValue;
        }
        if (valueList.size() < 0) {
            return null;
        }
        return (Pattern) valueList.get(0);
    }
}
