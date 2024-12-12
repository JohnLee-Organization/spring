/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 06:38
 */
package net.lizhaoweb.mikefox.digester;

import org.xml.sax.Attributes;

/**
 * Rule implementation that creates a new object and pushes it
 * onto the object stack.  When the element is complete, the
 * object will be popped
 * <p>
 * Created by jhon on 2024/6/8 6:38
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class ObjectCreateRule extends Rule {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an object create rule with the specified class name.
     *
     * @param className Java class name of the object to be created
     */
    public ObjectCreateRule(String className) {
        this(className, (String) null);
    }


    /**
     * Construct an object create rule with the specified class.
     *
     * @param clazz Java class name of the object to be created
     */
    public ObjectCreateRule(Class<?> clazz) {
        this(clazz.getName(), (String) null);
    }


    /**
     * Construct an object create rule with the specified class name and an
     * optional attribute name containing an override.
     *
     * @param className     Java class name of the object to be created
     * @param attributeName Attribute name which, if present, contains an
     *                      override of the class name to create
     */
    public ObjectCreateRule(String className, String attributeName) {
        this.className = className;
        this.attributeName = attributeName;
    }


    /**
     * Construct an object create rule with the specified class and an
     * optional attribute name containing an override.
     *
     * @param attributeName Attribute name which, if present, contains an
     * @param clazz         Java class name of the object to be created
     *                      override of the class name to create
     */
    public ObjectCreateRule(String attributeName, Class<?> clazz) {
        this(clazz.getName(), attributeName);
    }

    // ----------------------------------------------------- Instance Variables


    /**
     * The attribute containing an override class name if it is present.
     */
    protected String attributeName = null;


    /**
     * The Java class name of the object to be created.
     */
    protected String className = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Process the beginning of this element.
     *
     * @param namespace  the namespace URI of the matching element, or an
     *                   empty string if the parser is not namespace aware or the element has
     *                   no namespace
     * @param name       the local name if the parser is namespace aware, or just
     *                   the element name otherwise
     * @param attributes The attribute list for this element
     */
    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        // Identify the name of the class to instantiate
        String realClassName = className;
        if (attributeName != null) {
            String value = attributes.getValue(attributeName);
            if (value != null) {
                realClassName = value;
            }
        }
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[ObjectCreateRule]{" + digester.match + "}New " + realClassName);
        }
        // Instantiate the new object and push it on the context stack
        Class<?> clazz = digester.getClassLoader().loadClass(realClassName);
        Object instance = clazz.newInstance();
        digester.push(instance);
    }


    /**
     * Process the end of this element.
     *
     * @param namespace the namespace URI of the matching element, or an
     *                  empty string if the parser is not namespace aware or the element has
     *                  no namespace
     * @param name      the local name if the parser is namespace aware, or just
     *                  the element name otherwise
     */
    @Override
    public void end(String namespace, String name) throws Exception {
        Object top = digester.pop();
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[ObjectCreateRule]{" + digester.match + "} Pop " + top.getClass().getName());
        }
    }


    /**
     * Render a printable version of this Rule.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ObjectCreateRule[");
        sb.append("className=");
        sb.append(className);
        sb.append(", attributeName=");
        sb.append(attributeName);
        sb.append("]");
        return (sb.toString());
    }

}
