/*
 * Copyright (c) 2023, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.lic.truelicense
 * @date : 2023-08-03
 * @time : 15:53
 */
package net.lizhaoweb.listener;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.lic.truelicense.c.LicenseVerify;
import net.lizhaoweb.lic.truelicense.vo.LicenseVerifyParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 在项目启动的时候安装证书的Listener类
 * <p>
 * Created by Jhon.Lee on 8/3/2023 3:53 PM
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {

//    private static Logger logger = LoggerFactory.getLogger(LicenseCheckListener.class);

    /**
     * 证书subject
     */
    @Value("${license.subject:license}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias:publicCert}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass:}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath:/license/publicCerts.keystore}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath:/license/}")
    private String publicKeysStorePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            if (StringUtils.isNotBlank(licensePath)) {
                log.info("++++++++ 开始安装证书 ++++++++");

                LicenseVerifyParam param = new LicenseVerifyParam();
                param.setSubject(subject);
                param.setPublicAlias(publicAlias);
                param.setStorePass(storePass);
                param.setLicensePath(licensePath);
                param.setPublicKeysStorePath(publicKeysStorePath);

                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);

                log.info("++++++++ 证书安装结束 ++++++++");
            }
        }
    }
}
