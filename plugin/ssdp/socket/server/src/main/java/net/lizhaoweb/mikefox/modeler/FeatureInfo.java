/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 18:11
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.Getter;
import lombok.Setter;

import javax.management.MBeanFeatureInfo;
import java.io.Serializable;

/**
 * <p>Convenience base class for <code>AttributeInfo</code>,
 * <code>ConstructorInfo</code>, and <code>OperationInfo</code> classes
 * that will be used to collect configuration information for the
 * <code>ModelMBean</code> beans exposed for management.</p>
 * <p>
 * Created by jhon on 2024/6/7 18:11
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class FeatureInfo implements Serializable {

    /**
     * The human-readable description of this feature.
     */
    @Setter
    @Getter
    protected String description = null;
    /**
     * The name of this feature, which must be unique among features in the
     * same collection.
     */
    @Setter
    @Getter
    protected String name = null;
    protected MBeanFeatureInfo info = null;

    /**
     * The fully qualified Java class name of this element.
     */
    // all have type except Constructor
    @Setter
    @Getter
    protected String type = null;


}
