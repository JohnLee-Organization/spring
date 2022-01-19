/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.read
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:59
 */
package net.lizhaoweb.spring.mvc.uas.dao.read;

import net.lizhaoweb.spring.mvc.uas.model.Authorization;
import net.lizhaoweb.spring.mvc.uas.search.SearchAuthorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <h1>单元测试 [持久层:读]  - 权限</h1>
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
public class TestIAuthorizationReadMapper {

    @Autowired
    private IAuthorizationReadMapper readMapper;

    @Test
    public void existTable() {
        System.out.println(readMapper.existTable());
    }

    @Test
    public void findAll() {
        SearchAuthorization search = new SearchAuthorization();
        List<Authorization> list = readMapper.findAll(search);
        System.out.println(list);
    }

    @Test
    public void find() {
        SearchAuthorization search = new SearchAuthorization();
        search.setName("a1");
        Authorization bean = readMapper.find(search);
        System.out.println(bean);
    }

    @Test
    public void get() {
        Authorization bean = readMapper.get("id");
        System.out.println(bean);
        System.out.println(bean.getName());
    }
}
