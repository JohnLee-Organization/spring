/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-11
 * @time : 08:33
 */
package net.lizhaoweb.quentin;

import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * A <b>Manager</b> manages the pool of Sessions that are associated with a
 * particular Container.  Different Manager implementations may support
 * value-added features such as the persistent storage of session data,
 * as well as migrating sessions for distributable web applications.
 * <p>
 * In order for a <code>Manager</code> implementation to successfully operate
 * with a <code>Context</code> implementation that implements reloading, it
 * must obey the following constraints:
 * <ul>
 * <li>Must implement <code>Lifecycle</code> so that the Context can indicate
 *     that a restart is required.
 * <li>Must allow a call to <code>stop()</code> to be followed by a call to
 *     <code>start()</code> on the same <code>Manager</code> instance.
 * </ul>
 * <p>
 * Created by jhon on 2024/6/11 8:33
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface Manager {


    // ------------------------------------------------------------- Properties


    /**
     * Return the Container with which this Manager is associated.
     */
    Container getContainer();


    /**
     * Set the Container with which this Manager is associated.
     *
     * @param container The newly associated Container
     */
    void setContainer(Container container);


    /**
     * Return the distributable flag for the sessions supported by
     * this Manager.
     */
    boolean getDistributable();


    /**
     * Set the distributable flag for the sessions supported by this
     * Manager.  If this flag is set, all user data objects added to
     * sessions associated with this manager must implement Serializable.
     *
     * @param distributable The new distributable flag
     */
    void setDistributable(boolean distributable);


    /**
     * Return descriptive information about this Manager implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    String getInfo();


    /**
     * Return the default maximum inactive interval (in seconds)
     * for Sessions created by this Manager.
     */
    int getMaxInactiveInterval();


    /**
     * Set the default maximum inactive interval (in seconds)
     * for Sessions created by this Manager.
     *
     * @param interval The new default value
     */
    void setMaxInactiveInterval(int interval);


    /**
     * Gets the session id length (in bytes) of Sessions created by
     * this Manager.
     *
     * @return The session id length
     */
    int getSessionIdLength();


    /**
     * Sets the session id length (in bytes) for Sessions created by this
     * Manager.
     *
     * @param idLength The session id length
     */
    void setSessionIdLength(int idLength);


    /**
     * Returns the total number of sessions created by this manager.
     *
     * @return Total number of sessions created by this manager.
     */
    long getSessionCounter();


    /**
     * Sets the total number of sessions created by this manager.
     *
     * @param sessionCounter Total number of sessions created by this manager.
     */
    void setSessionCounter(long sessionCounter);


    /**
     * Gets the maximum number of sessions that have been active at the same
     * time.
     *
     * @return Maximum number of sessions that have been active at the same
     * time
     */
    int getMaxActive();


    /**
     * (Re)sets the maximum number of sessions that have been active at the
     * same time.
     *
     * @param maxActive Maximum number of sessions that have been active at
     *                  the same time.
     */
    void setMaxActive(int maxActive);


    /**
     * Gets the number of currently active sessions.
     *
     * @return Number of currently active sessions
     */
    int getActiveSessions();


    /**
     * Gets the number of sessions that have expired.
     *
     * @return Number of sessions that have expired
     */
    long getExpiredSessions();


    /**
     * Sets the number of sessions that have expired.
     *
     * @param expiredSessions Number of sessions that have expired
     */
    void setExpiredSessions(long expiredSessions);


    /**
     * Gets the number of sessions that were not created because the maximum
     * number of active sessions was reached.
     *
     * @return Number of rejected sessions
     */
    int getRejectedSessions();


    /**
     * Sets the number of sessions that were not created because the maximum
     * number of active sessions was reached.
     *
     * @param rejectedSessions Number of rejected sessions
     */
    void setRejectedSessions(int rejectedSessions);


    /**
     * Gets the longest time (in seconds) that an expired session had been
     * alive.
     *
     * @return Longest time (in seconds) that an expired session had been
     * alive.
     */
    int getSessionMaxAliveTime();


    /**
     * Sets the longest time (in seconds) that an expired session had been
     * alive.
     *
     * @param sessionMaxAliveTime Longest time (in seconds) that an expired
     *                            session had been alive.
     */
    void setSessionMaxAliveTime(int sessionMaxAliveTime);


    /**
     * Gets the average time (in seconds) that expired sessions had been
     * alive.
     *
     * @return Average time (in seconds) that expired sessions had been
     * alive.
     */
    int getSessionAverageAliveTime();


    /**
     * Sets the average time (in seconds) that expired sessions had been
     * alive.
     *
     * @param sessionAverageAliveTime Average time (in seconds) that expired
     *                                sessions had been alive.
     */
    void setSessionAverageAliveTime(int sessionAverageAliveTime);


    // --------------------------------------------------------- Public Methods


    /**
     * Add this Session to the set of active Sessions for this Manager.
     *
     * @param session Session to be added
     */
    void add(Session session);


    /**
     * Add a property change listener to this component.
     *
     * @param listener The listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);


    /**
     * Change the session ID of the current session to a new randomly generated
     * session ID.
     *
     * @param session The session to change the session ID for
     */
    void changeSessionId(Session session);


    /**
     * Get a session from the recycled ones or create a new empty one.
     * The PersistentManager manager does not need to create session data
     * because it reads it from the Store.
     */
    Session createEmptySession();


    /**
     * Construct and return a new session object, based on the default
     * settings specified by this Manager's properties.  The session
     * id specified will be used as the session id.
     * If a new session cannot be created for any reason, return
     * <code>null</code>.
     *
     * @param sessionId The session id which should be used to create the
     *                  new session; if <code>null</code>, the session
     *                  id will be assigned by this method, and available via the getId()
     *                  method of the returned session.
     * @throws IllegalStateException if a new session cannot be
     *                               instantiated for any reason
     */
    Session createSession(String sessionId);


    /**
     * Return the active Session, associated with this Manager, with the
     * specified session id (if any); otherwise return <code>null</code>.
     *
     * @param id The session id for the session to be returned
     * @throws IllegalStateException if a new session cannot be
     *                               instantiated for any reason
     * @throws IOException           if an input/output error occurs while
     *                               processing this request
     */
    Session findSession(String id) throws IOException;


    /**
     * Return the set of active Sessions associated with this Manager.
     * If this Manager has no active Sessions, a zero-length array is returned.
     */
    Session[] findSessions();


    /**
     * Load any currently active sessions that were previously unloaded
     * to the appropriate persistence mechanism, if any.  If persistence is not
     * supported, this method returns without doing anything.
     *
     * @throws ClassNotFoundException if a serialized class cannot be
     *                                found during the reload
     * @throws IOException            if an input/output error occurs
     */
    void load() throws ClassNotFoundException, IOException;


    /**
     * Remove this Session from the active Sessions for this Manager.
     *
     * @param session Session to be removed
     */
    void remove(Session session);


    /**
     * Remove a property change listener from this component.
     *
     * @param listener The listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);


    /**
     * Save any currently active sessions in the appropriate persistence
     * mechanism, if any.  If persistence is not supported, this method
     * returns without doing anything.
     *
     * @throws IOException if an input/output error occurs
     */
    void unload() throws IOException;

    /**
     * This method will be invoked by the context/container on a periodic
     * basis and allows the manager to implement
     * a method that executes periodic tasks, such as expiring sessions etc.
     */
    void backgroundProcess();

}
