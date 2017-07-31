/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : java
 * @Package : net.lizhaoweb.spring.mvc.uas.controller
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:29
 */
package net.lizhaoweb.spring.mvc.uas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <h1>控制层 - 首页</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月06日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Controller
public class IndexController extends UASAbstractController {

    /**
     * 首页
     */
    @RequestMapping(value = {"/index", "/"}, method = {RequestMethod.GET})
    public String webIndex() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "index");
        return viewPath;
    }

    @Override
    protected String getViewPrefix() {
        return "/common";
    }
}
