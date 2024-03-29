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
import net.lizhaoweb.sys.area.util.CoordinateTransformUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

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
    private static String[] PARSE_PATTERNS = {"yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
            "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private IDistrictService service;

    @Before
    public void init() {
        service = new DistrictService();
    }

    /**
     * 导入并分析中华人民共和国民政局给定的行政区划数据
     */
    @Test
    public void importAndAnalysisDistrict() throws ParseException {
        String importDataFile = "E:\\WorkSpace\\GitHub\\spring\\plugin\\administrative-area\\doc\\2012年基层自治组织统计代码（截止2012年12月31日）.Txt";
        Date statisticalDate = DateUtils.parseDate("2012-12-31", PARSE_PATTERNS);
        service.importAndAnalysisDistrict(importDataFile, statisticalDate);
    }

    @Test
    public void testGPSUtil() {
        double gcj02lat_1 = 30.569882783318254, gcj02lng_1 = 114.28274703443934;
//        double[] bd09Array_1 = GPSUtil.gcj02_To_Bd09(gcj02lat_1, gcj02lng_1);
//        System.out.printf("GCJ02[%s,%s] ==> BD09[%s,%s]\n", gcj02lat_1, gcj02lng_1, bd09Array_1[0], bd09Array_1[1]);
        double[] bd09Array_1 = CoordinateTransformUtil.gcj02tobd09(gcj02lng_1, gcj02lat_1);
        System.out.printf("GCJ02[%s,%s] ==> BD09[%s,%s]\n", gcj02lat_1, gcj02lng_1, bd09Array_1[1], bd09Array_1[0]);

        double db09lat_1 = 30.575664872946584, db09lng_1 = 114.28928462383861;
//        double[] dcj02Array_1 = GPSUtil.bd09_To_Gcj02(db09lat_1, db09lng_1);
//        System.out.printf("BD09[%s,%s] ==> GCJ02[%s,%s]\n", db09lat_1, db09lng_1, dcj02Array_1[0], dcj02Array_1[1]);
        double[] dcj02Array_1 = CoordinateTransformUtil.bd09togcj02(db09lng_1, db09lat_1);
        System.out.printf("BD09[%s,%s] ==> GCJ02[%s,%s]\n", db09lat_1, db09lng_1, dcj02Array_1[1], dcj02Array_1[0]);

//        System.out.println(new BigDecimal(1).add(new BigDecimal(2)).multiply(new BigDecimal(3)));
//        System.out.println(new BigDecimal(1).add(new BigDecimal(2).multiply(new BigDecimal(3))));
//        System.out.println(new BigDecimal(2).multiply(new BigDecimal(3)).add(new BigDecimal(1)));

//        BigDecimal aaa = new BigDecimal(2);
//        System.out.println(aaa.add(new BigDecimal(3)));
//        System.out.println(aaa.add(new BigDecimal(4)));
    }
}
