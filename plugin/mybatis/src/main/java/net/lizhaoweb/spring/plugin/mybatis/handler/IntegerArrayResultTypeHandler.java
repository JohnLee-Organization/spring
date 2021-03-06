/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : IntegerArrayResultTypeHandler.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.handler
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午5:53:33
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package net.lizhaoweb.spring.plugin.mybatis.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * <h1>类型处理器 - 整型数组</h1>
 * 
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年7月12日<br>
 *        Revision of last commit:$Revision$<br>
 *        Author of last commit:$Author$<br>
 *        Date of last commit:$Date$<br>
 *
 *
 */
@MappedTypes({ Integer[].class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class IntegerArrayResultTypeHandler extends AbstractString2ArrayResultTypeHandler<Integer> {

	@Override
	protected Integer[] getTargetArray(String columnValue) {
		if (columnValue == null) {
			return null;
		}
		if (columnValue.trim().length() < 1) {
			return new Integer[0];
		}
		List<Integer> result = new ArrayList<Integer>();
		for (String numberString : columnValue.split(",")) {
			if (numberString.matches("^\\d+$")) {
				try {
					result.add(Integer.valueOf(numberString));
				} catch (Exception e) {
				}
			}
		}
		return result.toArray(new Integer[0]);
	}
}
