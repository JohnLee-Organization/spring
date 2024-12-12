/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 18:49
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.Getter;
import lombok.Setter;

import javax.management.MBeanAttributeInfo;

/**
 * <p>Internal configuration information for an <code>Attribute</code>
 * descriptor.</p>
 * <p>
 * Created by jhon on 2024/6/7 18:49
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class AttributeInfo extends FeatureInfo {

    // ----------------------------------------------------- Instance Variables
    /**
     * The display name of this attribute.
     */
    @Setter
    @Getter
    protected String displayName = null;

    // Information about the method to use
    @Setter
    protected String getMethod = null;
    @Setter
    protected String setMethod = null;
    /**
     * Is this attribute readable by management applications?
     */
    @Setter
    @Getter
    protected boolean readable = true;
    /**
     * Is this attribute writable by management applications?
     */
    @Setter
    @Getter
    protected boolean writeable = true;
    /**
     * Is this a boolean attribute with an "is" getter?
     */
    @Setter
    @Getter
    protected boolean is = false;

    // ------------------------------------------------------------- Properties

    /**
     * The name of the property getter method, if non-standard.
     */
    public String getGetMethod() {
        if (getMethod == null) getMethod = getMethodName(getName(), true, isIs());
        return (this.getMethod);
    }


    /**
     * The name of the property setter method, if non-standard.
     */
    public String getSetMethod() {
        if (setMethod == null) setMethod = getMethodName(getName(), false, false);
        return (this.setMethod);
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Create and return a <code>ModelMBeanAttributeInfo</code> object that
     * corresponds to the attribute described by this instance.
     */
    MBeanAttributeInfo createAttributeInfo() {
        // Return our cached information (if any)
        if (info == null) {
            info = new MBeanAttributeInfo(getName(), getType(), getDescription(), isReadable(), isWriteable(), false);
        }
        return (MBeanAttributeInfo) info;
    }

    // -------------------------------------------------------- Private Methods


    /**
     * Create and return the name of a default property getter or setter
     * method, according to the specified values.
     *
     * @param name   Name of the property itself
     * @param getter Do we want a get method (versus a set method)?
     * @param is     If returning a getter, do we want the "is" form?
     */
    private String getMethodName(String name, boolean getter, boolean is) {
        StringBuilder sb = new StringBuilder();
        if (getter) {
            if (is) sb.append("is");
            else sb.append("get");
        } else sb.append("set");
        sb.append(Character.toUpperCase(name.charAt(0)));
        sb.append(name.substring(1));
        return (sb.toString());
    }

}
