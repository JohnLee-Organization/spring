/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 18:27
 */
package net.lizhaoweb.mikefox.digester;

import java.util.List;

/**
 * Public interface defining a collection of Rule instances (and corresponding
 * matching patterns) plus an implementation of a matching policy that selects
 * the rules that match a particular pattern of nested elements discovered
 * during parsing.
 * <p>
 * Created by jhon on 2024/6/8 18:27
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface Rules {


    // ------------------------------------------------------------- Properties


    /**
     * Return the Digester instance with which this Rules instance is
     * associated.
     */
    Digester getDigester();


    /**
     * Set the Digester instance with which this Rules instance is associated.
     *
     * @param digester The newly associated Digester instance
     */
    void setDigester(Digester digester);


    /**
     * Return the namespace URI that will be applied to all subsequently
     * added <code>Rule</code> objects.
     */
    String getNamespaceURI();


    /**
     * Set the namespace URI that will be applied to all subsequently
     * added <code>Rule</code> objects.
     *
     * @param namespaceURI Namespace URI that must match on all
     *                     subsequently added rules, or <code>null</code> for matching
     *                     regardless of the current namespace URI
     */
    void setNamespaceURI(String namespaceURI);


    // --------------------------------------------------------- Public Methods


    /**
     * Register a new Rule instance matching the specified pattern.
     *
     * @param pattern Nesting pattern to be matched for this Rule
     * @param rule    Rule instance to be registered
     */
    void add(String pattern, Rule rule);


    /**
     * Clear all existing Rule instance registrations.
     */
    void clear();


    /**
     * Return a List of all registered Rule instances that match the specified
     * nesting pattern, or a zero-length List if there are no matches.  If more
     * than one Rule instance matches, they <strong>must</strong> be returned
     * in the order originally registered through the <code>add()</code>
     * method.
     *
     * @param namespaceURI Namespace URI for which to select matching rules,
     *                     or <code>null</code> to match regardless of namespace URI
     * @param pattern      Nesting pattern to be matched
     */
    List<Rule> match(String namespaceURI, String pattern);


    /**
     * Return a List of all registered Rule instances, or a zero-length List
     * if there are no registered Rule instances.  If more than one Rule
     * instance has been registered, they <strong>must</strong> be returned
     * in the order originally registered through the <code>add()</code>
     * method.
     */
    List<Rule> rules();

}
