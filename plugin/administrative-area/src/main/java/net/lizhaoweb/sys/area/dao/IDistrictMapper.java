/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.dao
 * @date : 2021-09-01
 * @time : 11:16
 */
package net.lizhaoweb.sys.area.dao;

import net.lizhaoweb.sys.area.model.District;

/**
 * [持久] [接口] - 行政区划
 * <p>
 * Created by Admin on 2021/9/1 11:16
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
public interface IDistrictMapper {

    Long insert(District bean);

    Long update(District bean);

    District get(Long id);

    District deleteById(Long id);
}
