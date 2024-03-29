/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.controller
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 20:44
 */
package net.lizhaoweb.spring.mvc.uas.controller;

import lombok.Setter;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.model.Role;
import net.lizhaoweb.spring.mvc.uas.search.SearchRole;
import net.lizhaoweb.spring.mvc.uas.service.IRoleService;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <h1>控制层 - 角色</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequestMapping("/uas/role")
public class RoleController extends UASAbstractController {

    @Setter
    private IRoleService service;

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Object> save(HttpServletRequest request, HttpServletResponse response, Model model, ModelMap modelMap, Role bean) {
        DataDeliveryWrapper<Object> result = null;
        try {
            result = service.save(bean);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Object>(StatusCode.SERVER_INTERNAL_EXCEPTION, "保存角色异常", false);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Object> update(HttpServletRequest request, HttpServletResponse response, Model model, ModelMap modelMap, Role bean) {
        DataDeliveryWrapper<Object> result = null;
        try {
            result = service.update(bean);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Object>(StatusCode.SERVER_INTERNAL_EXCEPTION, "更新角色异常", false);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Object> delete(HttpServletRequest request, HttpServletResponse response, Model model, ModelMap modelMap, Long id) {
        DataDeliveryWrapper<Object> result = null;
        try {
            result = service.delete(id);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Object>(StatusCode.SERVER_INTERNAL_EXCEPTION, "删除角色异常", false);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/remove", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Object> remove(HttpServletRequest request, HttpServletResponse response, Model model, ModelMap modelMap, String ids) {
        DataDeliveryWrapper<Object> result = null;
        try {
            result = service.remove(ids);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Object>(StatusCode.SERVER_INTERNAL_EXCEPTION, "批量删除角色异常", false);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<PageResult<List<Role>>> list(HttpServletRequest request, HttpServletResponse response, Model model, ModelMap modelMap, SearchRole search) {
        DataDeliveryWrapper<PageResult<List<Role>>> result = null;
        try {
            result = service.pageList(search);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<PageResult<List<Role>>>(StatusCode.SERVER_INTERNAL_EXCEPTION, "获取角色列表异常", (PageResult<List<Role>>) null);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected String getViewPrefix() {
        return "/role";
    }
}
