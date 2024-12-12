/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 19:03
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.NoArgsConstructor;

import javax.management.MBeanParameterInfo;

/**
 * <p>Internal configuration information for a <code>Parameter</code>
 * descriptor.</p>
 * <p>
 * Created by jhon on 2024/6/7 19:03
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@NoArgsConstructor
public class ParameterInfo extends FeatureInfo {

    // ----------------------------------------------------------- Constructors


    /**
     * Create and return a <code>MBeanParameterInfo</code> object that
     * corresponds to the parameter described by this instance.
     */
    public MBeanParameterInfo createParameterInfo() {
        // Return our cached information (if any)
        if (info == null) {
            info = new MBeanParameterInfo(getName(), getType(), getDescription());
        }
        return (MBeanParameterInfo) info;
    }

}
