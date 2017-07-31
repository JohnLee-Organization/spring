/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 19:03
 */
package net.lizhaoweb.spring.mvc.uas.service.impl;

import net.lizhaoweb.spring.mvc.uas.service.IRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * <h1>单元测试 [业务层] - 角色</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:schema/spring/spring-app-web-uas-model.xml",
        "classpath*:schema/spring/spring-app-web-uas-mapper-mysql.xml",
        "classpath*:schema/spring/spring-app-web-uas-service.xml"
})
public class TestRoleService {

    @Resource(name = "uasRoleService")
    private IRoleService service;

    @Test
    public void createTable() {
        service.createTable();
    }

    @Test
    public void checkAndCreateTable() {
        service.checkAndCreateTable();
    }
}
