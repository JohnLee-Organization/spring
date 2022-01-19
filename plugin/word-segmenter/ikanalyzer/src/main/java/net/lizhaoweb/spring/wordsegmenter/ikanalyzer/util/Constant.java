/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : word-segmenter
 * @Package : org.wltea.analyzer.util
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 9:56
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.util;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public class Constant {

    /**
     * 配置
     */
    public static class Configure {

        /**
         * 路径
         */
        public static class Path {

            /**
             * 分词器配置文件路径
             */
            public static final String FILE = "config/IKAnalyzer.cfg.xml";

            /**
             * 字典
             */
            public static class Dictionary {

                /**
                 * 默认
                 */
                public static class Default {

                    /**
                     * 主字典
                     */
                    public static final String MAIN = "config/dictionary/main2012.dic";

                    /**
                     * 量词字典
                     */
                    public static final String QUANTIFIER = "config/dictionary/quantifier.dic";
                }
            }
        }

        /**
         * 属性
         */
        public static class Attribute {

            /**
             * 扩展字典
             */
            public static class ExtendDictionary {

                /**
                 * 键
                 */
                public static final String KEY = "ext_dict";
            }

            /**
             * 停词字典
             */
            public static class StopWordDictionary {

                /**
                 * 键
                 */
                public static final String KEY = "ext_stopwords";
            }
        }
    }

    /**
     * 标签
     */
    public static class Tag {

        /**
         * 中日韩文子分词器
         */
        public static final String CJK_SEGMENTER = "CJK_SEGMENTER";

        /**
         * 中文数量词子分词器
         */
        public static final String CN_QUANTIFIER_SEGMENTER = "CN_QUANTIFIER_SEGMENTER";

        /**
         * 英文字符及阿拉伯数字子分词器
         */
        public static final String LETTER_SEGMENTER = "LETTER_SEGMENTER";
    }
}
