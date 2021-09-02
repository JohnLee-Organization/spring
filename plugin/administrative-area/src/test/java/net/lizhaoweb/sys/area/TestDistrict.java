/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area
 * @date : 2021-09-02
 * @time : 10:17
 */
package net.lizhaoweb.sys.area;

import net.lizhaoweb.sys.area.service.IDistrictService;
import net.lizhaoweb.sys.area.service.impl.DistrictService;
import org.junit.Before;
import org.junit.Test;

/**
 * [测试] - 行政区划
 * <p>
 * Created by Admin on 2021/9/2 10:17
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
public class TestDistrict {

    private IDistrictService service;

    @Before
    public void init() {
        service = new DistrictService();
    }

    /**
     * 导入并分析中华人民共和国民政局给定的行政区划数据
     */
    @Test
    public void importAndAnalysisDistrict() {
        String importDataFile = "C:\\Users\\Admin\\Downloads\\2012年基层自治组织统计代码（截止2012年12月31日）.Txt";
        service.importAndAnalysisDistrict(importDataFile);
    }
}
