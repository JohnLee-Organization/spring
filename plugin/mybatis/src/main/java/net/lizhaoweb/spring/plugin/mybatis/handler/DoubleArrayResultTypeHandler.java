/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : DoubleArrayResultTypeHandler.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.handler
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午5:54:05
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
 * <h1>类型处理器 - 双精度浮点数组</h1>
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
@MappedTypes({ Double[].class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class DoubleArrayResultTypeHandler extends AbstractString2ArrayResultTypeHandler<Double> {

	@Override
	protected Double[] getTargetArray(String columnValue) {
		if (columnValue == null) {
			return null;
		}
		if (columnValue.trim().length() < 1) {
			return new Double[0];
		}
		List<Double> result = new ArrayList<Double>();
		for (String numberString : columnValue.split(",")) {
			if (numberString.matches("^\\d+\\.\\d+$")) {
				try {
					result.add(Double.valueOf(numberString));
				} catch (Exception e) {
				}
			}
		}
		return result.toArray(new Double[0]);
	}
}
