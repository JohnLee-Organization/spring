/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 18:29
 */
package net.lizhaoweb.mikefox.digester;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Default implementation of the <code>Rules</code> interface that supports
 * the standard rule matching behavior.  This class can also be used as a
 * base class for specialized <code>Rules</code> implementations.</p>
 *
 * <p>The matching policies implemented by this class support two different
 * types of pattern matching rules:</p>
 * <ul>
 * <li><em>Exact Match</em> - A pattern "a/b/c" exactly matches a
 *     <code>&lt;c&gt;</code> element, nested inside a <code>&lt;b&gt;</code>
 *     element, which is nested inside an <code>&lt;a&gt;</code> element.</li>
 * <li><em>Tail Match</em> - A pattern "&#42;/a/b" matches a
 *     <code>&lt;b&gt;</code> element, nested inside an <code>&lt;a&gt;</code>
 *      element, no matter how deeply the pair is nested.</li>
 * </ul>
 * <p>
 * Created by jhon on 2024/6/8 18:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class RulesBase implements Rules {


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of registered Rule instances, keyed by the matching pattern.
     * Each value is a List containing the Rules for that pattern, in the
     * order that they were originally registered.
     */
    protected HashMap<String, List<Rule>> cache = new HashMap<String, List<Rule>>();


    /**
     * The Digester instance with which this Rules instance is associated.
     */
    @Getter
    protected Digester digester = null;


    /**
     * The namespace URI for which subsequently added <code>Rule</code>
     * objects are relevant, or <code>null</code> for matching independent
     * of namespaces.
     */
    @Setter
    @Getter
    protected String namespaceURI = null;


    /**
     * The set of registered Rule instances, in the order that they were
     * originally registered.
     */
    protected ArrayList<Rule> rules = new ArrayList<Rule>();


    // ------------------------------------------------------------- Properties


    /**
     * Set the Digester instance with which this Rules instance is associated.
     *
     * @param digester The newly associated Digester instance
     */
    public void setDigester(Digester digester) {
        this.digester = digester;
        for (Rule item : rules) {
            item.setDigester(digester);
        }
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Register a new Rule instance matching the specified pattern.
     *
     * @param pattern Nesting pattern to be matched for this Rule
     * @param rule    Rule instance to be registered
     */
    public void add(String pattern, Rule rule) {
        // to help users who accidently add '/' to the end of their patterns
        int patternLength = pattern.length();
        if (patternLength > 1 && pattern.endsWith("/")) {
            pattern = pattern.substring(0, patternLength - 1);
        }
        List<Rule> list = cache.get(pattern);
        if (list == null) {
            list = new ArrayList<Rule>();
            cache.put(pattern, list);
        }
        list.add(rule);
        rules.add(rule);
        if (this.digester != null) {
            rule.setDigester(this.digester);
        }
        if (this.namespaceURI != null) {
            rule.setNamespaceURI(this.namespaceURI);
        }
    }


    /**
     * Clear all existing Rule instance registrations.
     */
    public void clear() {
        cache.clear();
        rules.clear();
    }


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
    public List<Rule> match(String namespaceURI, String pattern) {
        // List rulesList = (List) this.cache.get(pattern);
        List<Rule> rulesList = lookup(namespaceURI, pattern);
        if ((rulesList == null) || (rulesList.size() < 1)) {
            // Find the longest key, ie more discriminant
            String longKey = "";
            for (String key : this.cache.keySet()) {
                if (key.startsWith("*/")) {
                    if (pattern.equals(key.substring(2)) || pattern.endsWith(key.substring(1))) {
                        if (key.length() > longKey.length()) {
                            // rulesList = (List) this.cache.get(key);
                            rulesList = lookup(namespaceURI, key);
                            longKey = key;
                        }
                    }
                }
            }
        }
        if (rulesList == null) {
            rulesList = new ArrayList<Rule>();
        }
        return (rulesList);
    }


    /**
     * Return a List of all registered Rule instances, or a zero-length List
     * if there are no registered Rule instances.  If more than one Rule
     * instance has been registered, they <strong>must</strong> be returned
     * in the order originally registered through the <code>add()</code>
     * method.
     */
    public List<Rule> rules() {
        return (this.rules);
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return a List of Rule instances for the specified pattern that also
     * match the specified namespace URI (if any).  If there are no such
     * rules, return <code>null</code>.
     *
     * @param namespaceURI Namespace URI to match, or <code>null</code> to
     *                     select matching rules regardless of namespace URI
     * @param pattern      Pattern to be matched
     */
    protected List<Rule> lookup(String namespaceURI, String pattern) {
        // Optimize when no namespace URI is specified
        List<Rule> list = this.cache.get(pattern);
        if (list == null) {
            return (null);
        }
        if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
            return (list);
        }
        // Select only Rules that match on the specified namespace URI
        ArrayList<Rule> results = new ArrayList<Rule>();
        for (Rule item : list) {
            if ((namespaceURI.equals(item.getNamespaceURI())) || (item.getNamespaceURI() == null)) {
                results.add(item);
            }
        }
        return (results);
    }

}
