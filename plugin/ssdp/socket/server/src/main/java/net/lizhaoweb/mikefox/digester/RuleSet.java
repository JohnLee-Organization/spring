/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.digester
 * @date : 2024-06-08
 * @time : 10:40
 */
package net.lizhaoweb.mikefox.digester;

/**
 * <p>Public interface defining a shorthand means of configuring a complete
 * set of related <code>Rule</code> definitions, possibly associated with
 * a particular namespace URI, in one operation.  To use an instance of a
 * class that implements this interface:</p>
 * <ul>
 * <li>Create a concrete implementation of this interface.</li>
 * <li>Optionally, you can configure a <code>RuleSet</code> to be relevant
 *     only for a particular namespace URI by configuring the value to be
 *     returned by <code>getNamespaceURI()</code>.</li>
 * <li>As you are configuring your Digester instance, call
 *     <code>digester.addRuleSet()</code> and pass the RuleSet instance.</li>
 * <li>Digester will call the <code>addRuleInstances()</code> method of
 *     your RuleSet to configure the necessary rules.</li>
 * </ul>
 * <p>
 * Created by jhon on 2024/6/8 10:40
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface RuleSet {


    // ------------------------------------------------------------- Properties


    /**
     * Return the namespace URI that will be applied to all Rule instances
     * created from this RuleSet.
     */
    String getNamespaceURI();


    // --------------------------------------------------------- Public Methods


    /**
     * Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.
     *
     * @param digester Digester instance to which the new Rule instances
     *                 should be added.
     */
    void addRuleInstances(Digester digester);

}
