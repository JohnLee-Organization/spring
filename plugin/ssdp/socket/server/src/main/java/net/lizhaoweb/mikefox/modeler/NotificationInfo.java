/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 18:45
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.Getter;

import javax.management.MBeanNotificationInfo;

/**
 * <p>Internal configuration information for a <code>Notification</code>
 * descriptor.</p>
 * <p>
 * Created by jhon on 2024/6/7 18:45
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class NotificationInfo extends FeatureInfo {

    // ----------------------------------------------------- Instance Variables


    /**
     * The <code>ModelMBeanNotificationInfo</code> object that corresponds
     * to this <code>NotificationInfo</code> instance.
     */
    transient MBeanNotificationInfo info = null;
    /**
     * The set of notification types for this MBean.
     */
    @Getter
    protected String[] notifTypes = new String[0];

    // ------------------------------------------------------------- Properties


    /**
     * Override the <code>description</code> property setter.
     *
     * @param description The new description
     */
    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        this.info = null;
    }


    /**
     * Override the <code>name</code> property setter.
     *
     * @param name The new name
     */
    @Override
    public void setName(String name) {
        super.setName(name);
        this.info = null;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new notification type to the set managed by an MBean.
     *
     * @param notifType The new notification type
     */
    public void addNotifType(String notifType) {
        synchronized (notifTypes) {
            String[] results = new String[notifTypes.length + 1];
            System.arraycopy(notifTypes, 0, results, 0, notifTypes.length);
            results[notifTypes.length] = notifType;
            notifTypes = results;
            this.info = null;
        }
    }


    /**
     * Create and return a <code>ModelMBeanNotificationInfo</code> object that
     * corresponds to the attribute described by this instance.
     */
    public MBeanNotificationInfo createNotificationInfo() {
        // Return our cached information (if any)
        if (info != null) return (info);
        // Create and return a new information object
        info = new MBeanNotificationInfo(getNotifTypes(), getName(), getDescription());
        //Descriptor descriptor = info.getDescriptor();
        //addFields(descriptor);
        //info.setDescriptor(descriptor);
        return (info);
    }


    /**
     * Return a string representation of this notification descriptor.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("NotificationInfo[");
        sb.append("name=");
        sb.append(name);
        sb.append(", description=");
        sb.append(description);
        sb.append(", notifTypes=");
        sb.append(notifTypes.length);
        sb.append("]");
        return (sb.toString());
    }
}
