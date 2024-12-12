/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 06:27
 */
package net.lizhaoweb.mikefox.digester;

import net.lizhaoweb.mikefox.util.IntrospectionUtils;
import org.xml.sax.Attributes;

/**
 * Rule implementation that sets an individual property on the object at the
 * top of the stack, based on attributes with specified names.
 * <p>
 * Created by jhon on 2024/6/8 6:27
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class SetPropertyRule extends Rule {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a "set property" rule with the specified name and value
     * attributes.
     *
     * @param name  Name of the attribute that will contain the name of the
     *              property to be set
     * @param value Name of the attribute that will contain the value to which
     *              the property should be set
     */
    public SetPropertyRule(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // ----------------------------------------------------- Instance Variables


    /**
     * The attribute that will contain the property name.
     */
    protected String name = null;


    /**
     * The attribute that will contain the property value.
     */
    protected String value = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Process the beginning of this element.
     *
     * @param namespace  the namespace URI of the matching element, or an
     *                   empty string if the parser is not namespace aware or the element has
     *                   no namespace
     * @param theName    the local name if the parser is namespace aware, or just
     *                   the element name otherwise
     * @param attributes The attribute list for this element
     * @throws NoSuchMethodException if the bean does not
     *                               have a writable property of the specified name
     */
    @Override
    public void begin(String namespace, String theName, Attributes attributes) throws Exception {
        // Identify the actual property name and value to be used
        String actualName = null;
        String actualValue = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getLocalName(i);
            if ("".equals(name)) {
                name = attributes.getQName(i);
            }
            String value = attributes.getValue(i);
            if (name.equals(this.name)) {
                actualName = value;
            } else if (name.equals(this.value)) {
                actualValue = value;
            }
        }

        // Get a reference to the top object
        Object top = digester.peek();

        // Log some debugging information
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[SetPropertyRule]{" + digester.match + "} Set " + top.getClass().getName() + " property " + actualName + " to " + actualValue);
        }

        // Set the property (with conversion as necessary)
        if (!digester.isFakeAttribute(top, actualName) && !IntrospectionUtils.setProperty(top, actualName, actualValue) && digester.getRulesValidation()) {
            digester.log.warn("[SetPropertyRule]{" + digester.match + "} Setting property '" + name + "' to '" + value + "' did not find a matching property.");
        }
    }


    /**
     * Render a printable version of this Rule.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SetPropertyRule[");
        sb.append("name=");
        sb.append(name);
        sb.append(", value=");
        sb.append(value);
        sb.append("]");
        return (sb.toString());
    }

}
