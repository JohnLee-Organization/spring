/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : AbstractController.java
 * @Package : net.lizhaoweb.spring.mvc.core.controller
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月26日
 * @Time : 下午10:53:07
 */
package net.lizhaoweb.spring.mvc.core.controller;

import lombok.NoArgsConstructor;
import net.lizhaoweb.common.util.base.Constant;
import net.lizhaoweb.common.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>控制层 - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
public class AbstractController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * &lt;功能详细描述&gt;form表单提交 Date类型数据绑定
     *
     * @param binder 数据绑定对象。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * URL 编码。
     *
     * @param message 信息
     * @param charset 字符集
     * @return 编码后字符串。
     */
    protected String encode(String message, String charset) {
        String result = null;
        try {
            result = URLEncoder.encode(message, charset);
        } catch (UnsupportedEncodingException e) {
            result = message;
        }
        return result;
    }

    /**
     * 获取请求体
     *
     * @param request 请求对象
     * @return 返回请求体内容。
     */
    protected String getRequestBody(HttpServletRequest request) {
        String charset = request.getCharacterEncoding();
        if (StringUtil.isEmpty(charset)) {
            charset = Constant.Charset.UTF8;
        }

        int cacheSize = 4096;
        byte[] cache = new byte[cacheSize];
        int off = 0, len;
        StringBuffer contentStringBuffer = new StringBuffer();

        try {
            ServletInputStream servletInputStream = request.getInputStream();
            while ((len = servletInputStream.read(cache)) != -1) {
                contentStringBuffer.append(new String(cache, off, len, charset));
            }
            return contentStringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取客户端接收字符集。
     *
     * @param request 请求对象
     * @return 返回客户端接收字符集。
     */
    protected String getAcceptEncoding(HttpServletRequest request) {
        String acceptEncoding = request.getHeader("Accept-Encoding");
        if (StringUtil.isEmpty(acceptEncoding)) {
            acceptEncoding = Constant.Charset.UTF8;
        }
        return acceptEncoding;
    }

    /**
     * 向页面输出内容。
     *
     * @param response 响应对句。
     * @param message  要输出的内容。
     * @param charset  字符集。
     */
    protected void print(HttpServletResponse response, String message, String charset) {
        try {
            response.setCharacterEncoding(charset);
            String contentType = String.format("text/html; charset=%s", charset);
            response.setContentType(contentType);
            response.getWriter().write(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向页面输出内容。
     *
     * @param outputStream Servlet 输出流。
     * @param message      要输出的内容。
     * @param charset      字符集。
     */
    protected void print(ServletOutputStream outputStream, String message, String charset) {
        try {
            outputStream.write(message.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
