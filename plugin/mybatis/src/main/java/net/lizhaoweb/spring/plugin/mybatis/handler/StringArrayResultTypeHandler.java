/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : StringArrayResultTypeHandler.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.handler
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午5:19:47
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

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * <h1>类型处理器 - 字符串数组</h1>
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
@MappedTypes({ String[].class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class StringArrayResultTypeHandler extends AbstractString2ArrayResultTypeHandler<String> {

	@Override
	protected String[] getTargetArray(String columnValue) {
		if (columnValue == null)
			return null;
		return columnValue.split(",");
	}
}
