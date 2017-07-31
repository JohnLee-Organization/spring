/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : AbstractString2ArrayResultTypeHandler.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.handler
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午6:08:34
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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * <h1>类型处理器(抽象) - 数组</h1>
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
public abstract class AbstractString2ArrayResultTypeHandler<T> extends BaseTypeHandler<T[]> implements TypeHandler<T[]> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType) throws SQLException {
		// 由于BaseTypeHandler中已经把parameter为null的情况做了处理，所以这里我们就不用再判断parameter是否为空了，直接用就可以了
		StringBuffer result = new StringBuffer();
		for (T value : parameter) {
			result.append(value).append(",");
		}
		result.deleteCharAt(result.length() - 1);
		ps.setString(i, result.toString());
	}

	@Override
	public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getTargetArray(rs.getString(columnName));
	}

	@Override
	public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.getTargetArray(rs.getString(columnIndex));
	}

	@Override
	public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.getTargetArray(cs.getString(columnIndex));
	}

	protected abstract T[] getTargetArray(String columnValue);
}
