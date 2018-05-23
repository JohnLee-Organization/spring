/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.cache.mybatis.main
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:36
 */
package net.lizhaoweb.spring.cache.mybatis.main;

import net.lizhaoweb.spring.cache.mybatis.dao.read.IAccountReadMapper;
import net.lizhaoweb.spring.cache.mybatis.dao.write.IAccountWriteMapper;
import net.lizhaoweb.spring.cache.mybatis.model.Account;
import net.lizhaoweb.spring.cache.mybatis.search.SearchAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年05月23日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:schema/spring/spring-mvc-cache-redis.xml",
        "classpath:schema/spring/spring-app-web-uas-model.xml",
        "classpath:schema/spring/spring-app-web-uas-mapper-mysql.xml"
})
public class TestMybatisCache {

    @Autowired
    private IAccountWriteMapper writeMapper;

    @Autowired
    private IAccountReadMapper readMapper;

    @Test
    public void insert() {
        Account bean = new Account();
        bean.setId(null);
        bean.setLoginName("LoginName");
        bean.setPassword("Password");
        writeMapper.insert(bean);
        System.out.printf("aaaa");
    }

    @Test
    public void update() {
        Account bean = new Account();
        writeMapper.update(bean);
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
        search.setLoginName("admin");
        Account bean = readMapper.find(search);
        System.out.println(bean);
    }

    @Test
    public void get() {
        Account bean = readMapper.get(1L);
        System.out.println(bean);
    }
}
