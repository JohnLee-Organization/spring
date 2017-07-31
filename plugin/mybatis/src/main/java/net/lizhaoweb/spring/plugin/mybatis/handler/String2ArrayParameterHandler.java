/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : String2ArrayParameterHandler.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.handler
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午7:22:49
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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.executor.parameter.ParameterHandler;

/**
 * 
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 
 * @notes Created on 2016年7月12日<br>
 *        Revision of last commit:$Revision$<br>
 *        Author of last commit:$Author$<br>
 *        Date of last commit:$Date$<br>
 *
 *
 */
public class String2ArrayParameterHandler implements ParameterHandler {

	@Override
	public Object getParameterObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters(PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub

	}
}
