/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.write
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 18:23
 */
package net.lizhaoweb.spring.mvc.uas.dao.write;

import net.lizhaoweb.spring.mvc.uas.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <h1>单元测试 [持久层:写] - 角色</h1>
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
        "classpath*:schema/spring/spring-app-web-uas-mapper-mysql.xml"
})
public class TestIRoleWriteMapper {

    @Autowired
    private IRoleWriteMapper writeMapper;

    private Role bean = new Role();

    @Test
    public void createTable() {
        writeMapper.createTable();
    }

    @Test
    public void insert() {
        writeMapper.insert(bean);
    }

    @Test
    public void update() {
        writeMapper.update(bean);
    }
}
