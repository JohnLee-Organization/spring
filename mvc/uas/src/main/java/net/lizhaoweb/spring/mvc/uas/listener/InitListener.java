/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.listener
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:24
 */
package net.lizhaoweb.spring.mvc.uas.listener;

import lombok.Setter;
import net.lizhaoweb.spring.mvc.core.listener.AbstractListener;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.service.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * <h1>监听器 - 初始化</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class InitListener extends AbstractListener {

    @Setter
    private IAccountService accountService;

    @Setter
    private IRoleService roleService;

    @Setter
    private IAuthorizationService authorizationService;

    @Setter
    private IRoleAuthRelationService roleAuthRelationService;


    @Override
    protected void onApplicationLoad(ApplicationEvent applicationEvent) {
        if (!SystemConfigFactory.isUASTakeEffect()) {
            return;
        }
        logger.info("/================================== UAS Init Data Start ==================================\\");
        logger.info("\t\tcreate tables ...");
        accountService.checkAndCreateTable();
        roleService.checkAndCreateTable();
        authorizationService.checkAndCreateTable();
        roleAuthRelationService.checkAndCreateTable();
    }

    @Override
    protected void onApplicationLoad(WebApplicationContext webApplicationContext) {
        if (!SystemConfigFactory.isUASTakeEffect()) {
            return;
        }
        logger.info("zzz");
    }

    @Override
    protected void onApplicationLoad(ServletContext servletContext) {
        if (!SystemConfigFactory.isUASTakeEffect()) {
            return;
        }
        long adminId = 1L;
        Account account = accountService.get(adminId);
        if (account == null) {
            account = new Account(adminId, "admin", "admin", null);
            account.setName("超级管理员");
            account.setDescription("超级管理员用户，请不要删除");
            accountService.save(account);
        }
        logger.info("\\================================== UAS Init Data End ==================================/");
    }
}
