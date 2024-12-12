/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 19:46
 */
package net.lizhaoweb.mikefox.modeler;

import javax.management.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>Implementation of <code>NotificationBroadcaster</code> for attribute
 * change notifications.  This class is used by <code>BaseModelMBean</code> to
 * handle notifications of attribute change events to interested listeners.
 * </p>
 * <p>
 * Created by jhon on 2024/6/7 19:46
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class BaseNotificationBroadcaster implements NotificationBroadcaster {


    // ----------------------------------------------------------- Constructors


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of registered <code>BaseNotificationBroadcasterEntry</code>
     * entries.
     */
    protected ArrayList<BaseNotificationBroadcasterEntry> entries = new ArrayList<BaseNotificationBroadcasterEntry>();


    // --------------------------------------------------------- Public Methods


    /**
     * Add a notification event listener to this MBean.
     *
     * @param listener Listener that will receive event notifications
     * @param filter   Filter object used to filter event notifications
     *                 actually delivered, or <code>null</code> for no filtering
     * @param handback Handback object to be sent along with event
     *                 notifications
     * @throws IllegalArgumentException if the listener parameter is null
     */
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        synchronized (entries) {
            // Optimization to coalesce attribute name filters
            if (filter instanceof BaseAttributeFilter) {
                BaseAttributeFilter newFilter = (BaseAttributeFilter) filter;
                for (BaseNotificationBroadcasterEntry item : entries) {
                    if ((item.listener == listener) && (item.filter instanceof BaseAttributeFilter) && (item.handback == handback)) {
                        BaseAttributeFilter oldFilter = (BaseAttributeFilter) item.filter;
                        String[] newNames = newFilter.getNames();
                        String[] oldNames = oldFilter.getNames();
                        if (newNames.length == 0) {
                            oldFilter.clear();
                        } else {
                            if (oldNames.length != 0) {
                                for (String newName : newNames) oldFilter.addAttribute(newName);
                            }
                        }
                        return;
                    }
                }
            }
            // General purpose addition of a new entry
            entries.add(new BaseNotificationBroadcasterEntry(listener, filter, handback));
        }
    }


    /**
     * Return an <code>MBeanNotificationInfo</code> object describing the
     * notifications sent by this MBean.
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        return (new MBeanNotificationInfo[0]);
    }


    /**
     * Remove a notification event listener from this MBean.
     *
     * @param listener The listener to be removed (any and all registrations
     *                 for this listener will be eliminated)
     * @throws ListenerNotFoundException if this listener is not
     *                                   registered in the MBean
     */
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        synchronized (entries) {
            Iterator<BaseNotificationBroadcasterEntry> items = entries.iterator();
            while (items.hasNext()) {
                BaseNotificationBroadcasterEntry item = items.next();
                if (item.listener == listener) items.remove();
            }
        }
    }


    /**
     * Remove a notification event listener from this MBean.
     *
     * @param listener The listener to be removed (any and all registrations
     *                 for this listener will be eliminated)
     * @param handback Handback object to be sent along with event
     *                 notifications
     * @throws ListenerNotFoundException if this listener is not
     *                                   registered in the MBean
     */
    public void removeNotificationListener(NotificationListener listener, Object handback) throws ListenerNotFoundException {
        removeNotificationListener(listener);
    }


    /**
     * Remove a notification event listener from this MBean.
     *
     * @param listener The listener to be removed (any and all registrations
     *                 for this listener will be eliminated)
     * @param filter   Filter object used to filter event notifications
     *                 actually delivered, or <code>null</code> for no filtering
     * @param handback Handback object to be sent along with event
     *                 notifications
     * @throws ListenerNotFoundException if this listener is not
     *                                   registered in the MBean
     */
    public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
        removeNotificationListener(listener);
    }


    /**
     * Send the specified notification to all interested listeners.
     *
     * @param notification The notification to be sent
     */
    public void sendNotification(Notification notification) {
        synchronized (entries) {
            for (BaseNotificationBroadcasterEntry item : entries) {
                if ((item.filter != null) && (!item.filter.isNotificationEnabled(notification))) continue;
                item.listener.handleNotification(notification, item.handback);
            }
        }
    }

}


/**
 * Utility class representing a particular registered listener entry.
 */
class BaseNotificationBroadcasterEntry {

    public BaseNotificationBroadcasterEntry(NotificationListener listener, NotificationFilter filter, Object handback) {
        this.listener = listener;
        this.filter = filter;
        this.handback = handback;
    }

    public NotificationFilter filter = null;

    public Object handback = null;

    public NotificationListener listener = null;

}
