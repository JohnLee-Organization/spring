/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 10:24
 */
package net.lizhaoweb.mikefox.digester;

import org.xml.sax.Attributes;

/**
 * <p>Rule implementation that saves a parameter containing the
 * <code>Digester</code> matching path for use by a surrounding
 * <code>CallMethodRule</code>. This Rule is most useful when making
 * extensive use of wildcards in rule patterns.</p>
 * <p>
 * Created by jhon on 2024/6/8 10:24
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class PathCallParamRule extends Rule {

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a "call parameter" rule that will save the body text of this
     * element as the parameter value.
     *
     * @param paramIndex The zero-relative parameter number
     */
    public PathCallParamRule(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The zero-relative index of the parameter we are saving.
     */
    protected int paramIndex = 0;

    // --------------------------------------------------------- Public Methods


    /**
     * Process the start of this element.
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
        String param = getDigester().getMatch();
        if (param != null) {
            Object[] parameters = (Object[]) digester.peekParams();
            parameters[paramIndex] = param;
        }
    }

    /**
     * Render a printable version of this Rule.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PathCallParamRule[");
        sb.append("paramIndex=");
        sb.append(paramIndex);
        sb.append("]");
        return (sb.toString());
    }

}
