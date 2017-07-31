/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.ikanalyzer.query
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:40
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.query;

import lombok.Setter;
import net.lizhaoweb.spring.wordsegmenter.analyzer.IScheduler;
import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Single Word Multi Char Query Builder
 * IK分词算法专用
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SWMCQueryBuilder {

    @Setter
    @Autowired
    private static IScheduler ikSegmentScheduler;

    /**
     * 生成SWMCQuery
     *
     * @param fieldName 字段名
     * @param keywords  关键词
     * @param quickMode 是否 quick 模式
     * @return Lucene Query
     */
    public static Query create(String fieldName, String keywords, boolean quickMode) {
        if (fieldName == null || keywords == null) {
            throw new IllegalArgumentException("参数 fieldName 、 keywords 不能为null.");
        }
        //1.对keywords进行分词处理
        List<Lexeme> lexemes = doAnalyze(keywords);
        //2.根据分词结果，生成SWMCQuery
        Query _SWMCQuery = getSWMCQuery(fieldName, lexemes, quickMode);
        return _SWMCQuery;
    }

    /**
     * 分词切分，并返回结链表
     *
     * @param keywords 关键词
     * @return List&lt;Lexeme&gt;
     */
    private static List<Lexeme> doAnalyze(String keywords) {
        List<Lexeme> lexemes = new ArrayList<Lexeme>();
//        IScheduler ikSegmentScheduler = new IKSegmentScheduler();
        ikSegmentScheduler.setUseSmart(new StringReader(keywords), true);
        try {
            Lexeme l = null;
            while ((l = ikSegmentScheduler.next()) != null) {
                lexemes.add(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lexemes;
    }


    /**
     * 根据分词结果生成SWMC搜索
     *
     * @param fieldName 字段名
     * @param lexemes   lexeme 列表
     * @param quickMode 是否 quick 模式
     * @return Query
     */
    private static Query getSWMCQuery(String fieldName, List<Lexeme> lexemes, boolean quickMode) {
        //构造SWMC的查询表达式
        StringBuffer keywordBuffer = new StringBuffer();
        //精简的SWMC的查询表达式
        StringBuffer keywordBuffer_Short = new StringBuffer();
        //记录最后词元长度
        int lastLexemeLength = 0;
        //记录最后词元结束位置
        int lastLexemeEnd = -1;

        int shortCount = 0;
        int totalCount = 0;
        for (Lexeme l : lexemes) {
            totalCount += l.getLength();
            //精简表达式
            if (l.getLength() > 1) {
                keywordBuffer_Short.append(' ').append(l.getLexemeText());
                shortCount += l.getLength();
            }

            if (lastLexemeLength == 0) {
                keywordBuffer.append(l.getLexemeText());
            } else if (lastLexemeLength == 1 && l.getLength() == 1
                    && lastLexemeEnd == l.getBeginPosition()) {//单字位置相邻，长度为一，合并)
                keywordBuffer.append(l.getLexemeText());
            } else {
                keywordBuffer.append(' ').append(l.getLexemeText());

            }
            lastLexemeLength = l.getLength();
            lastLexemeEnd = l.getEndPosition();
        }

        //借助lucene queryparser 生成SWMC Query
        QueryParser qp = new QueryParser(Version.LUCENE_40, fieldName, new StandardAnalyzer(Version.LUCENE_40));
        qp.setDefaultOperator(QueryParser.AND_OPERATOR);
        qp.setAutoGeneratePhraseQueries(true);

        if (quickMode && (shortCount * 1.0f / totalCount) > 0.5f) {
            try {
                //System.out.println(keywordBuffer.toString());
                Query q = qp.parse(keywordBuffer_Short.toString());
                return q;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (keywordBuffer.length() > 0) {
                try {
                    //System.out.println(keywordBuffer.toString());
                    Query q = qp.parse(keywordBuffer.toString());
                    return q;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
