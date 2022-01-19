/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.read
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:58
 */
package net.lizhaoweb.spring.mvc.uas.dao.read;

import net.lizhaoweb.spring.mvc.uas.model.Role;
import net.lizhaoweb.spring.mvc.uas.search.SearchRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <h1>单元测试 [持久层:读] - 角色</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:schema/spring/spring-app-web-uas-model.xml",
        "classpath*:schema/spring/spring-app-web-uas-mapper-mysql.xml"
})
public class TestIRoleReadMapper {

    @Autowired
    private IRoleReadMapper readMapper;

    @Test
    public void existTable() {
        System.out.println(readMapper.existTable());
    }

    @Test
    public void findAll() {
        SearchRole search = new SearchRole();
        List<Role> list = readMapper.findAll(search);
        System.out.println(list);
    }

    @Test
    public void find() {
        SearchRole search = new SearchRole();
        search.setName("rn");
        Role bean = readMapper.find(search);
        System.out.println(bean);
    }

    @Test
    public void get() {
        Role bean = readMapper.get(1L);
        System.out.println(bean);
    }
}
