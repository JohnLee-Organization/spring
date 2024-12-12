/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 05:37
 */
package net.lizhaoweb.mikefox.digester;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.xml.sax.Attributes;

/**
 * Concrete implementations of this class implement actions to be taken when
 * a corresponding nested pattern of XML elements has been matched.
 * <p>
 * Created by jhon on 2024/6/8 5:37
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@NoArgsConstructor
public abstract class Rule {


    // ----------------------------------------------------------- Constructors


    // ----------------------------------------------------- Instance Variables


    /**
     * The Digester with which this Rule is associated.
     */
    @Setter
    @Getter
    protected Digester digester = null;


    /**
     * The namespace URI for which this Rule is relevant, if any.
     */
    @Setter
    @Getter
    protected String namespaceURI = null;


    // ------------------------------------------------------------- Properties


    // --------------------------------------------------------- Public Methods


    /**
     * This method is called when the beginning of a matching XML element
     * is encountered.
     *
     * @param namespace  the namespace URI of the matching element, or an
     *                   empty string if the parser is not namespace aware or the element has
     *                   no namespace
     * @param name       the local name if the parser is namespace aware, or just
     *                   the element name otherwise
     * @param attributes The attribute list of this element
     * @since Digester 1.4
     */
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        // The default implementation does nothing
    }


    /**
     * This method is called when the body of a matching XML element is
     * encountered.  If the element has no body, this method is not called at
     * all.
     *
     * @param namespace the namespace URI of the matching element, or an
     *                  empty string if the parser is not namespace aware or the element has
     *                  no namespace
     * @param name      the local name if the parser is namespace aware, or just
     *                  the element name otherwise
     * @param text      The text of the body of this element
     * @since Digester 1.4
     */
    public void body(String namespace, String name, String text) throws Exception {
        // The default implementation does nothing
    }


    /**
     * This method is called when the end of a matching XML element
     * is encountered.
     *
     * @param namespace the namespace URI of the matching element, or an
     *                  empty string if the parser is not namespace aware or the element has
     *                  no namespace
     * @param name      the local name if the parser is namespace aware, or just
     *                  the element name otherwise
     * @since Digester 1.4
     */
    public void end(String namespace, String name) throws Exception {
        // The default implementation does nothing
    }


    /**
     * This method is called after all parsing methods have been
     * called, to allow Rules to remove temporary data.
     */
    public void finish() throws Exception {
        // The default implementation does nothing
    }

}
