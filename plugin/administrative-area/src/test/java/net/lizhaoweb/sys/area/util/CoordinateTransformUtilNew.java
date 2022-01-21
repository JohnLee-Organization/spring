/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.util
 * @date : 2021-09-03
 * @time : 15:48
 */
package net.lizhaoweb.sys.area.util;

import java.math.BigDecimal;

/**
 * [工具] - 不同坐标系的经纬度转换 xxxx
 * <p>
 * Created by Admin on 2021/9/3 15:48
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
public class CoordinateTransformUtilNew {

    static BigDecimal C_N_100 = new BigDecimal(-100);
    static BigDecimal C_0_000003 = new BigDecimal("0.000003");
    static BigDecimal C_0_00002 = new BigDecimal("0.00002");
    static BigDecimal C_0_006 = new BigDecimal("0.006");
    static BigDecimal C_0_0065 = new BigDecimal("0.0065");
    static BigDecimal C_0_2 = new BigDecimal("0.2");
    static BigDecimal C_0_1 = new BigDecimal("0.1");
    static BigDecimal C_1 = new BigDecimal(1);
    static BigDecimal C_2 = new BigDecimal(2);
    static BigDecimal C_3 = new BigDecimal(3);
    static BigDecimal C_6 = new BigDecimal(6);
    static BigDecimal C_12 = new BigDecimal(12);
    static BigDecimal C_20 = new BigDecimal(20);
    static BigDecimal C_30 = new BigDecimal(30);
    static BigDecimal C_35 = new BigDecimal(35);
    static BigDecimal C_40 = new BigDecimal(40);
    static BigDecimal C_105 = new BigDecimal(105);
    static BigDecimal C_150 = new BigDecimal(150);
    static BigDecimal C_160 = new BigDecimal(160);
    static BigDecimal C_180 = new BigDecimal(180);
    static BigDecimal C_300 = new BigDecimal(300);
    static BigDecimal C_320 = new BigDecimal(320);
    static BigDecimal C_3000 = new BigDecimal(3000);

    static BigDecimal a = new BigDecimal(6378245);// 长半轴
    static BigDecimal ee = new BigDecimal("0.00669342162296594323");// 扁率
    static BigDecimal pi = new BigDecimal("3.1415926535897932384626");// π
    static BigDecimal x_pi = pi.multiply(C_3000).divide(C_180);


    /**
     * 百度坐标系(BD-09)转WGS坐标
     *
     * @param lng 百度坐标纬度
     * @param lat 百度坐标经度
     * @return WGS84坐标数组
     */
    public static double[] BD09ToWGS84(double lng, double lat) {
        double[] gcj = BD09ToGCJ02(lng, lat);
        double[] wgs84 = GCJ02ToWGS84(gcj[0], gcj[1]);
        return wgs84;
    }

    /**
     * WGS坐标转百度坐标系(BD-09)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 百度坐标数组
     */
    public static double[] WGS84ToBD09(double lng, double lat) {
        double[] gcj = WGS84ToGCJ02(lng, lat);
        double[] bd09 = GCJ02ToBD09(gcj[0], gcj[1]);
        return bd09;
    }

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * <p>
     * 谷歌、高德——>百度
     *
     * @param lng 火星坐标经度
     * @param lat 火星坐标纬度
     * @return 百度坐标数组
     */
    public static double[] GCJ02ToBD09(double lng, double lat) {
        BigDecimal x = new BigDecimal(lng), y = new BigDecimal(lat);
        BigDecimal z = BigDecimal.valueOf(Math.sin(y.multiply(x_pi).doubleValue())).multiply(C_0_00002).add(BigDecimal.valueOf(Math.sqrt(x.pow(2).add(y.pow(2)).doubleValue())));// sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi)
        BigDecimal theta = BigDecimal.valueOf(Math.cos(x.multiply(x_pi).doubleValue())).multiply(C_0_000003).add(BigDecimal.valueOf(Math.atan2(lat, lng)));// atan2(y, x) + 0.000003 * cos(x * x_pi)
        BigDecimal bd_lng = BigDecimal.valueOf(Math.cos(theta.doubleValue())).multiply(z).add(C_0_0065);// z * cos(theta) + 0.0065;
        BigDecimal bd_lat = BigDecimal.valueOf(Math.sin(theta.doubleValue())).multiply(z).add(C_0_006);// z * sin(theta) + 0.006
        return new double[]{bd_lng.doubleValue(), bd_lat.doubleValue()};
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * <p>
     * 百度——>谷歌、高德
     *
     * @param lon 百度坐标纬度
     * @param lat 百度坐标经度
     * @return 火星坐标数组
     */
    public static double[] BD09ToGCJ02(double lon, double lat) {
        BigDecimal x = new BigDecimal(lon).subtract(C_0_0065);// lon - 0.0065;
        BigDecimal y = new BigDecimal(lat).subtract(C_0_006);// lat - 0.006;
        BigDecimal z = BigDecimal.valueOf(Math.sin(y.multiply(x_pi).doubleValue())).multiply(C_0_00002).multiply(BigDecimal.valueOf(Math.sqrt(x.pow(2).add(y.pow(2)).doubleValue())));// sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        BigDecimal theta = BigDecimal.valueOf(Math.cos(x.multiply(x_pi).doubleValue())).multiply(C_0_000003).add(BigDecimal.valueOf(Math.atan2(y.doubleValue(), x.doubleValue())));// atan2(y, x) - 0.000003 * cos(x * x_pi);
        BigDecimal gg_lng = BigDecimal.valueOf(Math.cos(theta.doubleValue())).multiply(z);// z * Math.cos(theta.doubleValue());
        BigDecimal gg_lat = BigDecimal.valueOf(Math.sin(theta.doubleValue())).multiply(z);// z * Math.sin(theta.doubleValue());
        return new double[]{gg_lng.doubleValue(), gg_lat.doubleValue()};
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 火星坐标数组
     */
    public static double[] WGS84ToGCJ02(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new double[]{lng, lat};
        }
        BigDecimal x = new BigDecimal(lng), y = new BigDecimal(lat);
        BigDecimal dLat = transformLat(x.subtract(C_105), y.subtract(C_35));
        BigDecimal dLng = transformLng(x.subtract(C_105), y.subtract(C_35));
        BigDecimal radLat = y.multiply(pi).divide(C_180);// lat / 180.0 * pi;
        BigDecimal magic = C_1.subtract(BigDecimal.valueOf(Math.sin(radLat.doubleValue())).pow(2).multiply(ee));// Math.sin(radLat);magic = 1 - ee * magic * magic;
        BigDecimal sqrtMagic = new BigDecimal(Math.sqrt(magic.doubleValue()));
//        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
//        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        double mglat = lat + dLat;
//        double mglng = lng + dLng;
        BigDecimal mgLat = dLat.multiply(C_180).divide(C_1.subtract(ee).multiply(a).multiply(pi).divide(magic).divide(sqrtMagic)).add(y);
        BigDecimal mgLng = dLng.multiply(C_180).divide(new BigDecimal(Math.cos(radLat.doubleValue())).multiply(pi).multiply(a).divide(sqrtMagic)).add(x);
        return new double[]{mgLng.doubleValue(), mgLat.doubleValue()};
    }

    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param lng 火星坐标系的经度
     * @param lat 火星坐标系纬度
     * @return WGS84坐标数组
     */
    public static double[] GCJ02ToWGS84(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new double[]{lng, lat};
        }
        BigDecimal x = new BigDecimal(lng), y = new BigDecimal(lat);
        BigDecimal dLat = transformLat(x.subtract(C_105), y.subtract(C_35));
        BigDecimal dLng = transformLng(x.subtract(C_105), y.subtract(C_35));
        BigDecimal radLat = y.multiply(pi).divide(C_180); // lat / 180.0 * pi;
        BigDecimal magic = C_1.subtract(new BigDecimal(Math.sin(radLat.doubleValue())).pow(2).multiply(ee));// Math.sin(radLat);magic = 1 - ee * magic * magic;
        BigDecimal sqrtMagic = new BigDecimal(Math.sqrt(magic.doubleValue()));// Math.sqrt(magic);
//        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
//        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        double mglat = lat + dLat;
//        double mglng = lng + dLng;
        BigDecimal mgLat = dLat.multiply(C_180).divide(C_1.subtract(ee).multiply(a).multiply(pi).divide(magic.multiply(sqrtMagic))).add(y);
        BigDecimal mgLng = dLng.multiply(C_180).divide(new BigDecimal(Math.cos(radLat.doubleValue())).multiply(a).multiply(pi).divide(sqrtMagic)).add(x);
        return new double[]{x.multiply(C_2).subtract(mgLng).doubleValue(), y.multiply(C_2).subtract(mgLat).doubleValue()};
    }

    /**
     * 纬度转换
     *
     * @param x 经度
     * @param y 纬度
     * @return 纬度
     */
    private static BigDecimal transformLat(BigDecimal x, BigDecimal y) {
        // BigDecimal x = new BigDecimal(lng), y = new BigDecimal(lat);
        return BigDecimal.valueOf(Math.sqrt(Math.abs(x.doubleValue()))).multiply(C_0_2).add(x.multiply(y).multiply(C_0_1)).add(y.pow(2).multiply(C_0_2)).add(y.multiply(C_3)).add(x.multiply(C_2)).add(C_N_100) // -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
                .add(BigDecimal.valueOf(Math.sin(x.multiply(C_6).multiply(pi).doubleValue())).add(BigDecimal.valueOf(Math.sin(x.multiply(C_2).multiply(pi).doubleValue()))).multiply(C_20).multiply(C_2).divide(C_3))// ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
                .add(BigDecimal.valueOf(Math.sin(y.multiply(pi).divide(C_3).doubleValue())).multiply(C_40).add(BigDecimal.valueOf(Math.sin(y.multiply(pi).doubleValue())).multiply(C_20)).multiply(C_2).divide(C_3))// ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
                .add(BigDecimal.valueOf(Math.sin(y.multiply(pi).divide(C_12).doubleValue())).multiply(C_160).add(BigDecimal.valueOf(Math.sin(y.multiply(pi).divide(C_30).doubleValue())).multiply(C_320)).multiply(C_2).divide(C_3));// ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
    }

    /**
     * 经度转换
     *
     * @param x 经度
     * @param y 纬度
     * @return 经度
     */
    private static BigDecimal transformLng(BigDecimal x, BigDecimal y) {
        // BigDecimal x = new BigDecimal(lng), y = new BigDecimal(lat);
        return BigDecimal.valueOf(Math.sqrt(Math.abs(x.doubleValue()))).multiply(C_0_1).add(x.multiply(y).multiply(C_0_1)).add(x.pow(2).multiply(C_0_1)).add(y.multiply(C_2)).add(x).add(C_300)// 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
                .add(BigDecimal.valueOf(Math.sin(x.multiply(C_6).multiply(pi).doubleValue())).add(BigDecimal.valueOf(Math.sin(x.multiply(C_2).multiply(pi).doubleValue()))).multiply(C_20).multiply(C_2).divide(C_3))// ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
                .add(BigDecimal.valueOf(Math.sin(x.multiply(pi).doubleValue())).multiply(C_20).add(BigDecimal.valueOf(Math.sin(x.multiply(pi).divide(C_3).doubleValue())).multiply(C_40)).multiply(C_2).divide(C_3))// ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
                .add(BigDecimal.valueOf(Math.sin(x.multiply(pi).divide(C_12).doubleValue())).multiply(C_150).add(BigDecimal.valueOf(Math.sin(x.multiply(pi).divide(C_30).doubleValue())).multiply(C_300)).multiply(C_2).divide(C_3));// ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     *
     * @param lng 经度
     * @param lat 纬度
     * @return true
     */
    private static boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        } else return lat < 0.8293 || lat > 55.8271;
    }
}
