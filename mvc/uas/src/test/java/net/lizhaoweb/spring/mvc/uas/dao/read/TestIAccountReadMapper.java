/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.read
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:58
 */
package net.lizhaoweb.spring.mvc.uas.dao.read;

import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.search.SearchAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <h1>单元测试 [持久层:读] - 账户</h1>
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
public class TestIAccountReadMapper {

    @Autowired
    private IAccountReadMapper readMapper;

    @Test
    public void existTable() {
        System.out.println(readMapper.existTable());
    }

    @Test
    public void findAll() {
        SearchAccount search = new SearchAccount();
        List<Account> list = readMapper.findAll(search);
        System.out.println(list);
    }

    @Test
    public void find() {
        SearchAccount search = new SearchAccount();
        search.setName("admin");
        Account bean = readMapper.find(search);
        System.out.println(bean);
    }

    @Test
    public void get() {
        Account bean = readMapper.get(1L);
        System.out.println(bean);
    }
}
