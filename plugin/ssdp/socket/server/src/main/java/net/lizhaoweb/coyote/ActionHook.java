/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 15:30
 */
package net.lizhaoweb.coyote;

/**
 * Action hook. Actions represent the callback mechanism used by
 * coyote servlet containers to request operations on the coyote connectors.
 * Some standard actions are defined in ActionCode, however custom
 * actions are permitted.
 * <p>
 * The param object can be used to pass and return informations related with the
 * action.
 * <p>
 * <p>
 * This interface is typically implemented by ProtocolHandlers, and the param
 * is usually a Request or Response object.
 * <p>
 * Created by jhon on 2024/6/7 15:30
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface ActionHook {


    /**
     * Send an action to the connector.
     *
     * @param actionCode Type of the action
     * @param param      Action parameter
     */
    void action(ActionCode actionCode, Object param);

}
