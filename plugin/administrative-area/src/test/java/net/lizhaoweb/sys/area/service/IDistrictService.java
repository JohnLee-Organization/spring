/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.service
 * @date : 2021-09-02
 * @time : 10:15
 */
package net.lizhaoweb.sys.area.service;

/**
 * [服务] [接口] - 行政区划
 * <p>
 * Created by Admin on 2021/9/2 10:15
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
public interface IDistrictService {

    void importAndAnalysisDistrict(String importDataFile);
}
