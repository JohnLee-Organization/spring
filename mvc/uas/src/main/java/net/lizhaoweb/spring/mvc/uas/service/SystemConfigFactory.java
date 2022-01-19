/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 08:25
 */
package net.lizhaoweb.spring.mvc.uas.service;

import lombok.Getter;
import net.lizhaoweb.spring.mvc.uas.model.SystemConfig;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月04日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SystemConfigFactory {

    @Getter
    private static SystemConfig systemConfig;

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    /**
     * 用户权限系统是否生效。
     *
     * @return boolean
     */
    public static boolean isUASTakeEffect() {
        return (getSystemConfig() != null && getSystemConfig().isTakeEffect());
    }
}
