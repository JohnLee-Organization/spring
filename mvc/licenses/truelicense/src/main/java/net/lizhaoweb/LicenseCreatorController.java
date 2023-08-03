/*
 * Copyright (c) 2023, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb
 * @date : 2023-08-03
 * @time : 17:42
 */
package net.lizhaoweb;

import net.lizhaoweb.lic.truelicense.s.AbstractServerInfos;
import net.lizhaoweb.lic.truelicense.s.LicenseCreator;
import net.lizhaoweb.lic.truelicense.s.LinuxServerInfos;
import net.lizhaoweb.lic.truelicense.s.WindowsServerInfos;
import net.lizhaoweb.lic.truelicense.vo.LicenseCheckModel;
import net.lizhaoweb.lic.truelicense.vo.LicenseCreatorParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成证书
 * <p>
 * Created by Jhon.Lee on 8/3/2023 5:42 PM
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 0.0.1
 * @email 404644381@qq.com
 */
@RestController("net.lizhaoweb.LicenseCreatorController")
@RequestMapping("/license")
public class LicenseCreatorController {

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath:/license/license.lic}")
    private String licensePath;

    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Object> generateLicense(@RequestBody LicenseCreatorParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);

        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            resultMap.put("result", "ok");
            resultMap.put("msg", param);
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }

        return resultMap;
    }

}
