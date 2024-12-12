/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-11
 * @time : 08:29
 */
package net.lizhaoweb.quentin;

/**
 * A <b>Cluster</b> works as a Cluster client/server for the local host
 * Different Cluster implementations can be used to support different
 * ways to communicate within the Cluster. A Cluster implementation is
 * responsible for setting up a way to communicate within the Cluster
 * and also supply "ClientApplications" with <code>ClusterSender</code>
 * used when sending information in the Cluster and
 * <code>ClusterInfo</code> used for receiving information in the Cluster.
 * <p>
 * Created by jhon on 2024/6/11 8:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface Cluster {

    // ------------------------------------------------------------- Properties

    /**
     * Return descriptive information about this Cluster implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    String getInfo();

    /**
     * Return the name of the cluster that this Server is currently
     * configured to operate within.
     *
     * @return The name of the cluster associated with this server
     */
    String getClusterName();

    /**
     * Set the name of the cluster to join, if no cluster with
     * this name is present create one.
     *
     * @param clusterName The clustername to join
     */
    void setClusterName(String clusterName);

    /**
     * Set the Container associated with our Cluster
     *
     * @param container The Container to use
     */
    void setContainer(Container container);

    /**
     * Get the Container associated with our Cluster
     *
     * @return The Container associated with our Cluster
     */
    Container getContainer();

    // --------------------------------------------------------- Public Methods

    /**
     * Create a new manager which will use this cluster to replicate its
     * sessions.
     *
     * @param name Name (key) of the application with which the manager is
     *             associated
     */
    Manager createManager(String name);

    /**
     * Register a manager with the cluster. If the cluster is not responsible
     * for creating a manager, then the container will at least notify the
     * cluster that this manager is participating in the cluster.
     *
     * @param manager Manager
     */
    void registerManager(Manager manager);

    /**
     * Removes a manager from the cluster
     *
     * @param manager Manager
     */
    void removeManager(Manager manager);

    // --------------------------------------------------------- Cluster Wide Deployments


    /**
     * Execute a periodic task, such as reloading, etc. This method will be
     * invoked inside the classloading context of this container. Unexpected
     * throwables will be caught and logged.
     */
    void backgroundProcess();

}
