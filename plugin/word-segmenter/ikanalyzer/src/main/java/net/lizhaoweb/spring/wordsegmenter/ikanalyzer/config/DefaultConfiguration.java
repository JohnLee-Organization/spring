/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.config.ikanalyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 13:32
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.config;


import net.lizhaoweb.spring.wordsegmenter.config.IConfiguration;
import net.lizhaoweb.spring.wordsegmenter.ikanalyzer.util.Constant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * <h1>实现 - 默认</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Component
public class DefaultConfiguration implements IConfiguration {

    private Properties properties;

    //private String location;

    //是否使用smart方式分词
    private boolean useSmart;

    /**
     * 加载配置文件
     *
     * @param location 配置文件
     */
    public void setLocation(String location) {
        if (StringUtils.isEmpty(location)) {
            location = Constant.Configure.Path.FILE;
        }
        this.loadFile(location);
    }

    /**
     * 返回useSmart标志位
     * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @return useSmart
     */
    @Override
    public boolean useSmart() {
        return useSmart;
    }

    /**
     * 设置useSmart标志位
     * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @param useSmart 是否使用智能
     */
    @Override
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * 获取主词典路径
     *
     * @return String 主词典路径
     */
    @Override
    public String getMainDictionary() {
        return Constant.Configure.Path.Dictionary.Default.MAIN;
    }

    /**
     * 获取量词词典路径
     *
     * @return String 量词词典路径
     */
    @Override
    public String getQuantifierDicionary() {
        return Constant.Configure.Path.Dictionary.Default.QUANTIFIER;
    }

    /**
     * 获取扩展字典配置路径
     *
     * @return List&lt;String&gt; 相对类加载器的路径
     */
    @Override
    public List<String> getExtDictionarys() {
        List<String> extDictFiles = new ArrayList<String>(2);
        String extDictCfg = properties.getProperty(Constant.Configure.Attribute.ExtendDictionary.KEY);
        if (extDictCfg != null) {
            //使用;分割多个扩展字典配置
            String[] filePaths = extDictCfg.split(";");
            if (filePaths != null) {
                for (String filePath : filePaths) {
                    if (filePath != null && filePath.trim().length() > 0) {
                        extDictFiles.add(filePath.trim());
                    }
                }
            }
        }
        return extDictFiles;
    }

    /**
     * 获取扩展停止词典配置路径
     *
     * @return List&lt;String&gt; 相对类加载器的路径
     */
    @Override
    public List<String> getExtStopWordDictionarys() {
        List<String> extStopWordDictFiles = new ArrayList<String>(2);
        String extStopWordDictCfg = properties.getProperty(Constant.Configure.Attribute.StopWordDictionary.KEY);
        if (extStopWordDictCfg != null) {
            //使用;分割多个扩展字典配置
            String[] filePaths = extStopWordDictCfg.split(";");
            if (filePaths != null) {
                for (String filePath : filePaths) {
                    if (filePath != null && filePath.trim().length() > 0) {
                        extStopWordDictFiles.add(filePath.trim());
                    }
                }
            }
        }
        return extStopWordDictFiles;
    }

    private void loadFile(String location) {
        properties = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(location);
        if (input != null) {
            try {
                properties.loadFromXML(input);
            } catch (InvalidPropertiesFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
