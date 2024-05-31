/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:25
 */
package net.lizhaoweb.ssdp.servlet;

/**
 * This is the event class for notifications about changes to the attributes of the
 * servlet context of a web application.
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletContextAttributeListener
 * <p>
 * Created by jhon on 2024/5/30 15:25
 */
public class ServletContextAttributeEvent extends ServletContextEvent {

    private String name;
    private Object value;

    /**
     * Construct a ServletContextAttributeEvent from the given context for the
     * * given attribute name and attribute value.
     */
    public ServletContextAttributeEvent(ServletContext source, String name, Object value) {
        super(source);
        this.name = name;
        this.value = value;
    }

    /**
     * Return the name of the attribute that changed on the ServletContext.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of the attribute that has been added, removed, or replaced.
     * If the attribute was added, this is the value of the attribute. If the attribute was
     * removed, this is the value of the removed attribute. If the attribute was replaced, this
     * is the old value of the attribute.
     */
    public Object getValue() {
        return this.value;
    }

}
