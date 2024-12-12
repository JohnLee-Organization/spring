/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 08:55
 */
package net.lizhaoweb.mikefox.modeler;

import javax.management.ObjectName;
import java.util.List;

/**
 * Interface for modeler MBeans.
 * <p>
 * This is the main entry point into modeler. It provides methods to create
 * and manipulate model mbeans and simplify their use.
 * <p>
 * Starting with version 1.1, this is no longer a singleton and the static
 * methods are strongly deprecated. In a container environment we can expect
 * different applications to use different registries.
 * <p>
 * Created by jhon on 2024/6/7 8:55
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public interface RegistryMBean {

    /**
     * Load an extended mlet file. The source can be an URL, File or
     * InputStream.
     * <p>
     * All mbeans will be instantiated, registered and the attributes will be
     * set. The result is a list of ObjectNames.
     *
     * @param source InputStream or URL of the file
     * @param cl     ClassLoader to be used to load the mbeans, or null to use the
     *               default JMX mechanism ( i.e. all registered loaders )
     * @return List of ObjectName for the loaded mbeans
     * @throws Exception Exception
     */
    List<ObjectName> loadMBeans(Object source, ClassLoader cl) throws Exception;

    /**
     * Invoke an operation on a set of mbeans.
     *
     * @param mbeans    List of ObjectNames
     * @param operation Operation to perform. Typically "init" "start" "stop" or "destroy"
     * @param failFirst Behavior in case of exceptions - if false we'll ignore
     *                  errors
     * @throws Exception Exception
     */
    void invoke(List<ObjectName> mbeans, String operation, boolean failFirst) throws Exception;

    /**
     * Register a bean by creating a modeler mbean and adding it to the
     * MBeanServer.
     * <p>
     * If metadata is not loaded, we'll look up and read a file named
     * "mbeans-descriptors.ser" or "mbeans-descriptors.xml" in the same package
     * or parent.
     * <p>
     * If the bean is an instance of DynamicMBean. it's metadata will be converted
     * to a model mbean and we'll wrap it - so modeler services will be supported
     * <p>
     * If the metadata is still not found, introspection will be used to extract
     * it automatically.
     * <p>
     * If an mbean is already registered under this name, it'll be first
     * unregistered.
     * <p>
     * If the component implements MBeanRegistration, the methods will be called.
     * If the method has a method "setRegistry" that takes a RegistryMBean as
     * parameter, it'll be called with the current registry.
     *
     * @param bean  Object to be registered
     * @param oname Name used for registration
     * @param type  The type of the mbean, as declared in mbeans-descriptors. If
     *              null, the name of the class will be used. This can be used as a hint or
     *              by subclasses.
     */
    void registerComponent(Object bean, String oname, String type) throws Exception;

    /**
     * Unregister a component. We'll first check if it is registered,
     * and mask all errors. This is mostly a helper.
     *
     * @param oname oname
     */
    void unregisterComponent(String oname);


    /**
     * Return an int ID for faster access. Will be used for notifications
     * and for other operations we want to optimize.
     *
     * @param domain Namespace
     * @param name   Type of the notification
     * @return An unique id for the domain:name combination
     */
    int getId(String domain, String name);


    /**
     * Reset all metadata cached by this registry. Should be called
     * to support reloading. Existing mbeans will not be affected or modified.
     * <p>
     * It will be called automatically if the Registry is unregistered.
     */
    void stop();

    /**
     * Load descriptors. The source can be a File, URL pointing to an
     * mbeans-descriptors.xml.
     * <p>
     * Also ( experimental for now ) a ClassLoader - in which case META-INF/ will
     * be used.
     *
     * @param source source
     */
    void loadMetadata(Object source) throws Exception;

}
