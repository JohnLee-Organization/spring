/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.util
 * @date : 2021-09-03
 * @time : 16:52
 */
package net.lizhaoweb.sys.area.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lizhaoweb.sys.area.model.PinYinAPIResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * [工具] - 拼音
 * <p>
 * Created by Admin on 2021/9/3 16:52
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
public class PinYinUtils {

    private static final Map<Character, TreeSet<String>> HAN_ZI_MAP = new HashMap<>();

    public static final String PIN_YIN_SEPARATOR = "|";

    public static final String PIN_YIN_SEPARATOR_REGEX = "\\|";

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

    /**
     * 从网络接口把汉字转拼音
     *
     * @param hanZi 汉字
     * @return 拼音
     */
    public static String convertPinYinFromAPI(String hanZi, String separator) {
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

    /**
     * 从本地字典把汉字转拼音
     *
     * @param hanZiString 汉字
     * @return 拼音
     */
    public static String convertPinYinFromLocal(String hanZiString, String separator) {
        StringBuilder result = new StringBuilder();
        char[] hanZiChars = hanZiString.toCharArray();
        for (int index = 0; index < hanZiChars.length; index++) {
            char hanZi = hanZiChars[index];
            if ('（' == hanZi) {
                result.append('(').append(separator);
                continue;
            } else if ('）' == hanZi) {
                result.append(')').append(separator);
                continue;
            } else if ('【' == hanZi) {
                result.append('[').append(separator);
                continue;
            } else if ('】' == hanZi) {
                result.append(']').append(separator);
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
            result.append(separator);
        }
        return result.length() > separator.length() && result.substring(result.length() - separator.length()).equals(separator) ? result.substring(0, result.length() - separator.length()) : result.toString();
    }

    /**
     * 把拼音的首字母进行大写处理
     *
     * @param apiPinYin      要转换的拼音字符串
     * @param separatorRegex 要转换的拼音字符串的分隔串的正则表达式
     * @param newSeparator   返回字符串的分隔串
     * @return 转换后的拼音
     */
    public static String capitalizePinYin(String apiPinYin, String separatorRegex, String newSeparator) {
        String[] dataSplit = apiPinYin.split(separatorRegex);
        StringBuilder result = new StringBuilder();
        for (String _hanZi : dataSplit) {
            result.append(StringUtils.capitalize(_hanZi)).append(newSeparator);
        }
        return result.length() > newSeparator.length() && result.substring(result.length() - newSeparator.length()).equals(newSeparator) ? result.substring(0, result.length() - newSeparator.length()) : result.toString();
    }

    /**
     * 通过拼音获取简拼，且首字母大写
     *
     * @param apiPinYin      要转换的拼音字符串
     * @param separatorRegex 要转换的拼音字符串的分隔串的正则表达式
     * @param newSeparator   返回字符串的分隔串
     * @return 转换后的拼音
     */
    public static String capitalizeShortPinYin(String apiPinYin, String separatorRegex, String newSeparator) {
        String[] dataSplit = apiPinYin.split(separatorRegex);
        StringBuilder result = new StringBuilder();
        for (String _hanZi : dataSplit) {
            if (_hanZi == null) {
                throw new NullPointerException();
            }
            if (_hanZi.length() > 1 && _hanZi.charAt(1) == 'h') {
                result.append(StringUtils.capitalize(_hanZi.substring(0, 2)));
            } else {
                result.append(_hanZi.substring(0, 1).toUpperCase());
            }
            result.append(newSeparator);
        }
        return result.length() > newSeparator.length() && result.substring(result.length() - newSeparator.length()).equals(newSeparator) ? result.substring(0, result.length() - newSeparator.length()) : result.toString();
    }
}
