/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.service.impl
 * @date : 2021-09-02
 * @time : 10:16
 */
package net.lizhaoweb.sys.area.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.sys.area.dao.IDistrictMapper;
import net.lizhaoweb.sys.area.model.District;
import net.lizhaoweb.sys.area.service.IDistrictService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * [服务] [实现] - 行政区划
 * <p>
 * Created by Admin on 2021/9/2 10:16
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class DistrictService implements IDistrictService {

    private static final Map<Character, TreeSet<String>> HAN_ZI_MAP = new HashMap<>();
    private static final char PIN_YIN_SPLIT_CHAR = '|';

    private IDistrictMapper districtMapper;

    private int pCount = 0, cCount = 0, dCount = 0, tCount = 0, vCount, nCount = 0;

    static {
        FileInputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = new FileInputStream("E:\\WorkSpace\\GitHub\\spring\\plugin\\administrative-area\\src\\test\\resources\\pinyin.txt");
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                char hanZi = line.substring(0, 1).toCharArray()[0];
                String[] pinYin = line.substring(1).split(",");
                TreeSet<String> pinYinList = HAN_ZI_MAP.get(hanZi);
                if (pinYinList == null) {
                    pinYinList = new TreeSet<>();
                }
                for (String onePinYin : pinYin) {
                    String _onePinYin = onePinYin.substring(0, onePinYin.length() - 1);
                    pinYinList.add(_onePinYin);
                }
                HAN_ZI_MAP.put(hanZi, pinYinList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DistrictService() {
        try {
            String resource = "net/lizhaoweb/sys/area/dao/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            try (SqlSession session = sqlSessionFactory.openSession()) {
                districtMapper = session.getMapper(IDistrictMapper.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入并分析中华人民共和国民政局给定的行政区划数据
     */
    public void importAndAnalysisDistrict(String importDataFile) {
        FileInputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            Pattern pcdPattern = Pattern.compile("省|市|县$");
            long startTime = System.currentTimeMillis();
            File importFile = new File(importDataFile);
            inputStream = new FileInputStream(importFile);
            inputStreamReader = new InputStreamReader(inputStream, "GBK");
            reader = new BufferedReader(inputStreamReader);
            String line;
            int dataRowCount = 0, lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().length() == 0) {
                    continue;
                }
                String[] lineSplit = line.split("\\|");
                String districtCode = lineSplit[0].trim();
                String fullName = lineSplit[1].trim();
                dataRowCount++;
                if (districtCode.length() < 6) {
                    log.warn(line);
                    continue;
                }
                if ("000000".equals(districtCode.substring(6))) {
                    districtCode = districtCode.substring(0, 6);
                }
                District district = new District();
                district.setDistrictCode(districtCode);
                district.setFullName(fullName);
                if (fullName.length() > 2 && !fullName.endsWith("自治县") && pcdPattern.matcher(fullName).find()) {
                    district.setName(fullName.substring(0, fullName.length() - 1));
                } else {
                    district.setName(fullName);
                }
                if (district.getName().length() > 0) {
                    String apiPinYin = convertPinYinFromLocal(district.getName());
                    district.setPinYin(getPinYin(apiPinYin));
                    district.setShortPinYin(getShortPinYin(apiPinYin));
                }
                analysisDistrict(district);
//                districtMapper.insert(district);
            }
            System.out.printf("%d个省，%d个市，%d个县，%d个乡镇，%d个村庄，%d个未知\t%d\t\t", pCount, cCount, dCount, tCount, vCount, nCount, pCount + cCount + dCount + tCount + vCount + nCount);
            System.out.printf("共%d行，数据%d条，耗时%s秒\n", lineCount, dataRowCount, (System.currentTimeMillis() - startTime) / 1000D);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void analysisDistrict(District district) {
        if (district.getDistrictCode().length() > 6) {// 县级以下
            analysisDistrict4DownCounty(district);
        } else {// 县级以上
            analysisDistrict4UpCounty(district);
        }
    }

    private void analysisDistrict4DownCounty(District district) {
        if (district.getDistrictCode().endsWith("000000")) {
            nCount++;
        } else if (district.getDistrictCode().endsWith("000")) {
            district.setType(4);
            district.setHierarchy(5);
            tCount++;
        } else {
            district.setType(5);
            district.setHierarchy(6);
            vCount++;
        }
    }

    private void analysisDistrict4UpCounty(District district) {
        if (district.getDistrictCode().endsWith("0000")) {
            district.setType(1);
            district.setHierarchy(2);
            pCount++;
        } else if (district.getDistrictCode().endsWith("00")) {
            district.setType(2);
            district.setHierarchy(3);
            cCount++;
        } else {
            district.setType(3);
            district.setHierarchy(4);
            dCount++;
        }
        System.out.println(district);
    }

    // 从网络接口把汉字转拼音
    private String convertPinYinFromAPI(String hanZi) {
        StringBuilder result = new StringBuilder();
        for (String oneHanZi : hanZi.split("")) {
            BufferedReader reader = null;
            InputStream is = null;
            try {
                String httpUrl = "https://msp.ccc.cmbchina.com/ita/new/cdn/api/OscarService/GetPinYin?request=" + oneHanZi;
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String strRead = null;
                StringBuilder sbf = new StringBuilder();
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                }
                reader.close();
                is.close();
                ObjectMapper objectMapper = new ObjectMapper();
                PinYinAPIResponse pinYinAPIResponse = objectMapper.readValue(sbf.toString(), PinYinAPIResponse.class);
                result.append(pinYinAPIResponse.getData()).append("|");
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly(is);
            }
        }
        return result.toString();
    }

    // 从本地字典把汉字转拼音
    private String convertPinYinFromLocal(String hanZiString) {
        StringBuilder result = new StringBuilder();
        char[] hanZiChars = hanZiString.toCharArray();
        for (int index = 0; index < hanZiChars.length; index++) {
            char hanZi = hanZiChars[index];
            if ('（' == hanZi) {
                result.append('(').append(PIN_YIN_SPLIT_CHAR);
                continue;
            } else if ('）' == hanZi) {
                result.append(')').append(PIN_YIN_SPLIT_CHAR);
                continue;
            } else if ('【' == hanZi) {
                result.append('[').append(PIN_YIN_SPLIT_CHAR);
                continue;
            } else if ('】' == hanZi) {
                result.append(']').append(PIN_YIN_SPLIT_CHAR);
                continue;
            }
            TreeSet<String> pinYinList = HAN_ZI_MAP.get(hanZi);
            if (pinYinList == null) {
                result.append(hanZi);
                continue;
            }
            if (pinYinList.size() == 1) {
                result.append(pinYinList.first());
            } else if (pinYinList.size() > 1) {
                if ('乐' == hanZi && index > 0 && '音' == hanZiString.charAt(index - 1)) {
                    result.append(pinYinList.last());
                } else if ('乐' == hanZi && index > 0 && '演' == hanZiString.charAt(index - 1)) {
                    result.append("yao");
                } else {
                    result.append(pinYinList.first());
                }
            }
            result.append(PIN_YIN_SPLIT_CHAR);
        }
        return result.length() > 0 && result.charAt(result.length() - 1) == PIN_YIN_SPLIT_CHAR ? result.substring(0, result.length() - 1) : result.toString();
    }

    // 把转换后的拼音进行处理
    private String getPinYin(String apiPinYin) {
        String[] dataSplit = apiPinYin.split("\\|");
        StringBuilder pinyinStringBuilder = new StringBuilder();
        for (String _hanZi : dataSplit) {
            pinyinStringBuilder.append(StringUtils.capitalize(_hanZi));
        }
        return pinyinStringBuilder.toString();
    }

    // 通过转换后的拼音获取简拼
    private String getShortPinYin(String apiPinYin) {
        String[] dataSplit = apiPinYin.split("\\|");
        StringBuilder pinyinStringBuilder = new StringBuilder();
        for (String _hanZi : dataSplit) {
            if (_hanZi == null) {
                throw new NullPointerException();
            }
            if (_hanZi.length() > 1 && _hanZi.charAt(1) == 'h') {
                pinyinStringBuilder.append(StringUtils.capitalize(_hanZi.substring(0, 2)));
            } else {
                pinyinStringBuilder.append(_hanZi.substring(0, 1).toUpperCase());
            }
        }
        return pinyinStringBuilder.toString();
    }
}

@Data
class PinYinAPIResponse {

    @JsonProperty("ReturnCode")
    private String returnCode;

    @JsonProperty("ReturnMessage")
    private String returnMessage;

    @JsonProperty("Data")
    private String data;
}