/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.controller
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:51
 */
package net.lizhaoweb.spring.mvc.uas.controller;

import net.lizhaoweb.spring.mvc.core.controller.AbstractController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class UASAbstractController extends AbstractController {

    protected final String VIEW_PATH_FORMAT = "%s/%s";

    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public ModelAndView add() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "add");
        ModelAndView modelAndView = new ModelAndView(viewPath);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = {RequestMethod.GET})
    public ModelAndView edit() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "edit");
        ModelAndView modelAndView = new ModelAndView(viewPath);
        return modelAndView;
    }

    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ModelAndView view() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "view");
        ModelAndView modelAndView = new ModelAndView(viewPath);
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ModelAndView list() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "list");
        ModelAndView modelAndView = new ModelAndView(viewPath);
        return modelAndView;
    }

    protected abstract String getViewPrefix();
}
