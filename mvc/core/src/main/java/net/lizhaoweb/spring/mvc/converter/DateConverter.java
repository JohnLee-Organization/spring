/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : DateConverter.java
 * @Package : net.lizhaoweb.spring.mvc.converter
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月27日
 * @Time : 上午10:17:21
 */
package net.lizhaoweb.spring.mvc.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>转换器 - 日期</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class DateConverter implements Converter<String, Date> {

    /**
     * 转换。
     *
     * @param text 请求传递的值
     * @return Date
     */
    @Override
    public Date convert(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
