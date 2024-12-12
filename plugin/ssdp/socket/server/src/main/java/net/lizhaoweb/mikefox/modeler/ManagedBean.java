/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 08:58
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.Getter;
import lombok.Setter;

import javax.management.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Internal configuration information for a managed bean (MBean)
 * descriptor.</p>
 * <p>
 * Created by jhon on 2024/6/7 8:58
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class ManagedBean implements Serializable {

    private static final String BASE_MBEAN = "net.lizhaoweb.mikefox.modeler.BaseModelMBean";
    // ----------------------------------------------------- Instance Variables
    static final Object[] NO_ARGS_PARAM = new Object[0];
    static final Class<?>[] NO_ARGS_PARAM_SIG = new Class[0];


    /**
     * The <code>ModelMBeanInfo</code> object that corresponds
     * to this <code>ManagedBean</code> instance.
     */
    transient MBeanInfo info = null;

    private Map<String, AttributeInfo> attributes = new HashMap<String, AttributeInfo>();

    private Map<String, OperationInfo> operations = new HashMap<String, OperationInfo>();

    /**
     * The fully qualified name of the Java class of the MBean
     * described by this descriptor.  If not specified, the standard JMX
     * class (<code>javax.management.modelmbean.RequiredModeLMBean</code>)
     * will be utilized.
     */
    @Getter
    protected String className = BASE_MBEAN;
    /**
     * The human-readable description of this MBean.
     */
    //protected ConstructorInfo constructors[] = new ConstructorInfo[0];
    @Getter
    protected String description = null;
    /**
     * The (optional) <code>ObjectName</code> domain in which this MBean
     * should be registered in the MBeanServer.
     */
    @Setter
    @Getter
    protected String domain = null;
    /**
     * The (optional) group to which this MBean belongs.
     */
    @Setter
    @Getter
    protected String group = null;
    /**
     * The name of this managed bean, which must be unique among all
     * MBeans managed by a particular MBeans server.
     */
    @Getter
    protected String name = null;

    /**
     * The collection of notifications for this MBean.
     */
    //protected List fields = new ArrayList();
    @Getter
    protected NotificationInfo[] notifications = new NotificationInfo[0];
    /**
     * The fully qualified name of the Java class of the resource
     * implementation class described by the managed bean described
     * by this descriptor.
     */
    @Getter
    protected String type = null;

    /**
     * Constructor. Will add default attributes.
     */
    public ManagedBean() {
        AttributeInfo ai = new AttributeInfo();
        ai.setName("modelerType");
        ai.setDescription("Type of the modeled resource. Can be set only once");
        ai.setType("java.lang.String");
        ai.setWriteable(false);
        addAttribute(ai);
    }

    // ------------------------------------------------------------- Properties


    /**
     * The collection of attributes for this MBean.
     */
    public AttributeInfo[] getAttributes() {
        AttributeInfo[] result = new AttributeInfo[attributes.size()];
        attributes.values().toArray(result);
        return result;
    }

    public void setClassName(String className) {
        this.className = className;
        this.info = null;
    }


    public void setDescription(String description) {
        this.description = description;
        this.info = null;
    }

    public void setName(String name) {
        this.name = name;
        this.info = null;
    }


    /**
     * The collection of operations for this MBean.
     */
    public OperationInfo[] getOperations() {
        OperationInfo[] result = new OperationInfo[operations.size()];
        operations.values().toArray(result);
        return result;
    }

    public void setType(String type) {
        this.type = type;
        this.info = null;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new attribute to the set of attributes for this MBean.
     *
     * @param attribute The new attribute descriptor
     */
    public void addAttribute(AttributeInfo attribute) {
        attributes.put(attribute.getName(), attribute);
    }


    /**
     * Add a new notification to the set of notifications for this MBean.
     *
     * @param notification The new notification descriptor
     */
    public void addNotification(NotificationInfo notification) {
        synchronized (notifications) {
            NotificationInfo[] results = new NotificationInfo[notifications.length + 1];
            System.arraycopy(notifications, 0, results, 0, notifications.length);
            results[notifications.length] = notification;
            notifications = results;
            this.info = null;
        }
    }


    /**
     * Add a new operation to the set of operations for this MBean.
     *
     * @param operation The new operation descriptor
     */
    public void addOperation(OperationInfo operation) {
        operations.put(operation.getName(), operation);
    }


    /**
     * Create and return a <code>ModelMBean</code> that has been
     * preconfigured with the <code>ModelMBeanInfo</code> information
     * for this managed bean, but is not associated with any particular
     * managed resource.  The returned <code>ModelMBean</code> will
     * <strong>NOT</strong> have been registered with our
     * <code>MBeanServer</code>.
     *
     * @throws InstanceNotFoundException  if the managed resource
     *                                    object cannot be found
     * @throws MBeanException             if a problem occurs instantiating the
     *                                    <code>ModelMBean</code> instance
     * @throws RuntimeOperationsException if a JMX runtime error occurs
     */
    public DynamicMBean createMBean() throws InstanceNotFoundException, MBeanException, RuntimeOperationsException {
        return (createMBean(null));
    }


    /**
     * Create and return a <code>ModelMBean</code> that has been
     * preconfigured with the <code>ModelMBeanInfo</code> information
     * for this managed bean, and is associated with the specified
     * managed object instance.  The returned <code>ModelMBean</code>
     * will <strong>NOT</strong> have been registered with our
     * <code>MBeanServer</code>.
     *
     * @param instance Instanced of the managed object, or <code>null</code>
     *                 for no associated instance
     * @throws InstanceNotFoundException  if the managed resource
     *                                    object cannot be found
     * @throws MBeanException             if a problem occurs instantiating the
     *                                    <code>ModelMBean</code> instance
     * @throws RuntimeOperationsException if a JMX runtime error occurs
     */
    public DynamicMBean createMBean(Object instance) throws InstanceNotFoundException, MBeanException, RuntimeOperationsException {
        BaseModelMBean mbean = null;
        // Load the ModelMBean implementation class
        if (getClassName().equals(BASE_MBEAN)) {
            // Skip introspection
            mbean = new BaseModelMBean();
        } else {
            Class<?> clazz = null;
            Exception ex = null;
            try {
                clazz = Class.forName(getClassName());
            } catch (Exception e) {
                //
            }
            if (clazz == null) {
                try {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    if (cl != null) clazz = cl.loadClass(getClassName());
                } catch (Exception e) {
                    ex = e;
                }
            }
            if (clazz == null) {
                throw new MBeanException(ex, "Cannot load ModelMBean class " + getClassName());
            }
            try {
                // Stupid - this will set the default minfo first....
                mbean = (BaseModelMBean) clazz.newInstance();
            } catch (RuntimeOperationsException e) {
                throw e;
            } catch (Exception e) {
                throw new MBeanException(e, "Cannot instantiate ModelMBean of class " + getClassName());
            }
        }
        mbean.setManagedBean(this);
        // Set the managed resource (if any)
        if (instance != null) mbean.setManagedResource(instance, "ObjectReference");
        return (mbean);
    }


    /**
     * Create and return a <code>ModelMBeanInfo</code> object that
     * describes this entire managed bean.
     */
    MBeanInfo getMBeanInfo() {

        // Return our cached information (if any)
        if (info != null) return (info);

        // Create subordinate information descriptors as required
        AttributeInfo[] attrs = getAttributes();
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[attrs.length];
        for (int i = 0; i < attrs.length; i++)
            attributes[i] = attrs[i].createAttributeInfo();

        OperationInfo[] opers = getOperations();
        MBeanOperationInfo[] operations = new MBeanOperationInfo[opers.length];
        for (int i = 0; i < opers.length; i++)
            operations[i] = opers[i].createOperationInfo();

        NotificationInfo[] notifs = getNotifications();
        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[notifs.length];
        for (int i = 0; i < notifs.length; i++)
            notifications[i] = notifs[i].createNotificationInfo();


        // Construct and return a new ModelMBeanInfo object
        info = new MBeanInfo(getClassName(), getDescription(), attributes, new MBeanConstructorInfo[]{}, operations, notifications);

        return (info);
    }


    /**
     * Return a string representation of this managed bean.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ManagedBean[");
        sb.append("name=");
        sb.append(name);
        sb.append(", className=");
        sb.append(className);
        sb.append(", description=");
        sb.append(description);
        if (group != null) {
            sb.append(", group=");
            sb.append(group);
        }
        sb.append(", type=");
        sb.append(type);
        sb.append("]");
        return (sb.toString());
    }

    Method getGetter(String aname, BaseModelMBean mbean, Object resource) throws AttributeNotFoundException, MBeanException, ReflectionException {
        // TODO: do we need caching ? JMX is for management, it's not supposed to require lots of performance.
        Method m = null; // (Method)getAttMap.get( name );
        AttributeInfo attrInfo = attributes.get(aname);
        // Look up the actual operation to be used
        if (attrInfo == null) throw new AttributeNotFoundException(" Cannot find attribute " + aname + " for " + resource);

        String getMethod = attrInfo.getGetMethod();
        if (getMethod == null) throw new AttributeNotFoundException("Cannot find attribute " + aname + " get method name");

        Object object = null;
        NoSuchMethodException exception = null;
        try {
            object = mbean;
            m = object.getClass().getMethod(getMethod, NO_ARGS_PARAM_SIG);
        } catch (NoSuchMethodException e) {
            exception = e;
        }
        if (m == null && resource != null) {
            try {
                object = resource;
                m = object.getClass().getMethod(getMethod, NO_ARGS_PARAM_SIG);
                exception = null;
            } catch (NoSuchMethodException e) {
                exception = e;
            }
        }
        if (exception != null) throw new ReflectionException(exception, "Cannot find getter method " + getMethod);
        return m;
    }

    public Method getSetter(String aname, BaseModelMBean bean, Object resource) throws AttributeNotFoundException, MBeanException, ReflectionException {
        // Cache may be needed for getters, but it is a really bad idea for setters, this is far
        // less frequent.
        Method m = null;//(Method)setAttMap.get( name );
        AttributeInfo attrInfo = attributes.get(aname);
        if (attrInfo == null) throw new AttributeNotFoundException(" Cannot find attribute " + aname);

        // Look up the actual operation to be used
        String setMethod = attrInfo.getSetMethod();
        if (setMethod == null) throw new AttributeNotFoundException("Cannot find attribute " + aname + " set method name");

        String argType = attrInfo.getType();

        Class<?>[] signature = new Class[]{BaseModelMBean.getAttributeClass(argType)};

        Object object = null;
        NoSuchMethodException exception = null;
        try {
            object = bean;
            m = object.getClass().getMethod(setMethod, signature);
        } catch (NoSuchMethodException e) {
            exception = e;
        }
        if (m == null && resource != null) {
            try {
                object = resource;
                m = object.getClass().getMethod(setMethod, signature);
                exception = null;
            } catch (NoSuchMethodException e) {
                exception = e;
            }
        }
        if (exception != null) throw new ReflectionException(exception, "Cannot find setter method " + setMethod + " " + resource);
        return m;
    }

    public Method getInvoke(String aname, Object[] params, String[] signature, BaseModelMBean bean, Object resource) throws MBeanException, ReflectionException {
        Method method = null;
        if (params == null) params = new Object[0];
        if (signature == null) signature = new String[0];
        if (params.length != signature.length) throw new RuntimeOperationsException(new IllegalArgumentException("Inconsistent arguments and signature"), "Inconsistent arguments and signature");

        // Acquire the ModelMBeanOperationInfo information for
        // the requested operation
        OperationInfo opInfo = operations.get(aname);
        if (opInfo == null) throw new MBeanException(new ServiceNotFoundException("Cannot find operation " + aname), "Cannot find operation " + aname);

        // Prepare the signature required by Java reflection APIs
        // FIXME - should we use the signature from opInfo?
        Class<?>[] types = new Class[signature.length];
        for (int i = 0; i < signature.length; i++) {
            types[i] = BaseModelMBean.getAttributeClass(signature[i]);
        }

        // Locate the method to be invoked, either in this MBean itself
        // or in the corresponding managed resource
        // FIXME - Accessible methods in superinterfaces?
        Object object = null;
        Exception exception = null;
        try {
            object = bean;
            method = object.getClass().getMethod(aname, types);
        } catch (NoSuchMethodException e) {
            exception = e;
        }
        try {
            if ((method == null) && (resource != null)) {
                object = resource;
                method = object.getClass().getMethod(aname, types);
            }
        } catch (NoSuchMethodException e) {
            exception = e;
        }
        if (method == null) {
            throw new ReflectionException(exception, "Cannot find method " + aname + " with this signature");
        }
        return method;
    }

}
