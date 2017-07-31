/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : IntConverter.java
 * @Package : net.lizhaoweb.spring.mvc.converter
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月27日
 * @Time : 上午10:15:03
 */
package net.lizhaoweb.spring.mvc.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * <h1>转换器 - 整形</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class IntConverter implements Converter<String, Integer> {

    /**
     * 转换。
     *
     * @param text 请求传递的值
     * @return Integer
     */
    @Override
    public Integer convert(String text) {
        if (StringUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                Integer value = Integer.parseInt(text);
                return value;
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
