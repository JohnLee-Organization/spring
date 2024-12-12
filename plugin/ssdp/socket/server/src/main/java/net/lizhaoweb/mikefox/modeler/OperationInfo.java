/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler
 * @date : 2024-06-07
 * @time : 18:58
 */
package net.lizhaoweb.mikefox.modeler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import java.util.Locale;

/**
 * <p>Internal configuration information for an <code>Operation</code>
 * descriptor.</p>
 * <p>
 * Created by jhon on 2024/6/7 18:58
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@NoArgsConstructor
@SuppressWarnings("unused")
public class OperationInfo extends FeatureInfo {

    // ----------------------------------------------------------- Constructors


    // ----------------------------------------------------- Instance Variables

    /**
     * The "impact" of this operation, which should be a (case-insensitive)
     * string value "ACTION", "ACTION_INFO", "INFO", or "UNKNOWN".
     */
    @Getter
    protected String impact = "UNKNOWN";
    /**
     * The role of this operation ("getter", "setter", "operation", or
     * "constructor").
     */
    @Setter
    @Getter
    protected String role = "operation";
    protected ParameterInfo[] parameters = new ParameterInfo[0];


    // ------------------------------------------------------------- Properties


    public void setImpact(String impact) {
        if (impact == null) this.impact = null;
        else this.impact = impact.toUpperCase(Locale.ENGLISH);
    }


    /**
     * The fully qualified Java class name of the return type for this
     * operation.
     */
    public String getReturnType() {
        if (type == null) {
            type = "void";
        }
        return type;
    }

    public void setReturnType(String returnType) {
        this.type = returnType;
    }

    /**
     * The set of parameters for this operation.
     */
    public ParameterInfo[] getSignature() {
        return (this.parameters);
    }

    // --------------------------------------------------------- Public Methods


    /**
     * Add a new parameter to the set of arguments for this operation.
     *
     * @param parameter The new parameter descriptor
     */
    public void addParameter(ParameterInfo parameter) {
        synchronized (parameters) {
            ParameterInfo[] results = new ParameterInfo[parameters.length + 1];
            System.arraycopy(parameters, 0, results, 0, parameters.length);
            results[parameters.length] = parameter;
            parameters = results;
            this.info = null;
        }
    }


    /**
     * Create and return a <code>ModelMBeanOperationInfo</code> object that
     * corresponds to the attribute described by this instance.
     */
    MBeanOperationInfo createOperationInfo() {
        // Return our cached information (if any)
        if (info == null) {
            // Create and return a new information object
            int impact = MBeanOperationInfo.UNKNOWN;
            if ("ACTION".equals(getImpact())) impact = MBeanOperationInfo.ACTION;
            else if ("ACTION_INFO".equals(getImpact())) impact = MBeanOperationInfo.ACTION_INFO;
            else if ("INFO".equals(getImpact())) impact = MBeanOperationInfo.INFO;
            info = new MBeanOperationInfo(getName(), getDescription(), getMBeanParameterInfo(), getReturnType(), impact);
        }
        return (MBeanOperationInfo) info;
    }

    protected MBeanParameterInfo[] getMBeanParameterInfo() {
        ParameterInfo[] params = getSignature();
        MBeanParameterInfo[] parameters = new MBeanParameterInfo[params.length];
        for (int i = 0; i < params.length; i++)
            parameters[i] = params[i].createParameterInfo();
        return parameters;
    }

}
