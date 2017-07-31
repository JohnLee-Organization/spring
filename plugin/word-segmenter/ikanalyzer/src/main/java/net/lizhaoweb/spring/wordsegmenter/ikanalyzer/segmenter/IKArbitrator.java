/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter.ikanalyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:03
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.segmenter;

import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import net.lizhaoweb.spring.wordsegmenter.model.LexemePath;
import net.lizhaoweb.spring.wordsegmenter.model.QuickSortSet;
import net.lizhaoweb.spring.wordsegmenter.model.QuickSortSetLinkedCell;
import net.lizhaoweb.spring.wordsegmenter.segmenter.IAnalyzeContext;
import net.lizhaoweb.spring.wordsegmenter.segmenter.IArbitrator;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.TreeSet;

/**
 * <h1>实现 - IK分词歧义裁决器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Component
public class IKArbitrator implements IArbitrator {

    /**
     * 分词歧义处理
     *
     * @param context  上下文
     * @param useSmart 是否使用智能
     */
    @Override
    public void process(IAnalyzeContext context, boolean useSmart) {
        QuickSortSet orgLexemes = context.getOrgLexemes();
        Lexeme orgLexeme = orgLexemes.pollFirst();

        LexemePath crossPath = new LexemePath();
        while (orgLexeme != null) {
            if (!crossPath.addCrossLexeme(orgLexeme)) {
                //找到与crossPath不相交的下一个crossPath
                if (crossPath.size() == 1 || !useSmart) {
                    //crossPath没有歧义 或者 不做歧义处理
                    //直接输出当前crossPath
                    context.addLexemePath(crossPath);
                } else {
                    //对当前的crossPath进行歧义处理
                    QuickSortSetLinkedCell headCell = crossPath.getHead();
                    LexemePath judgeResult = this.judge(headCell, crossPath.getPathLength());
                    //输出歧义处理结果judgeResult
                    context.addLexemePath(judgeResult);
                }

                //把orgLexeme加入新的crossPath中
                crossPath = new LexemePath();
                crossPath.addCrossLexeme(orgLexeme);
            }
            orgLexeme = orgLexemes.pollFirst();
        }

        //处理最后的path
        if (crossPath.size() == 1 || !useSmart) {
            //crossPath没有歧义 或者 不做歧义处理
            //直接输出当前crossPath
            context.addLexemePath(crossPath);
        } else {
            //对当前的crossPath进行歧义处理
            QuickSortSetLinkedCell headCell = crossPath.getHead();
            LexemePath judgeResult = this.judge(headCell, crossPath.getPathLength());
            //输出歧义处理结果judgeResult
            context.addLexemePath(judgeResult);
        }
    }

    /**
     * 歧义识别
     *
     * @param lexemeCell     歧义路径链表头
     * @param fullTextLength 歧义路径文本长度
     * @return LexemePath
     */
    private LexemePath judge(QuickSortSetLinkedCell lexemeCell, int fullTextLength) {
        //候选路径集合
        TreeSet<LexemePath> pathOptions = new TreeSet<LexemePath>();
        //候选结果路径
        LexemePath option = new LexemePath();

        //对crossPath进行一次遍历,同时返回本次遍历中有冲突的Lexeme栈
        Stack<QuickSortSetLinkedCell> lexemeStack = this.forwardPath(lexemeCell, option);

        //当前词元链并非最理想的，加入候选路径集合
        pathOptions.add(option.copy());

        //存在歧义词，处理
        QuickSortSetLinkedCell c = null;
        while (!lexemeStack.isEmpty()) {
            c = lexemeStack.pop();
            //回滚词元链
            this.backPath(c.getLexeme(), option);
            //从歧义词位置开始，递归，生成可选方案
            this.forwardPath(c, option);
            pathOptions.add(option.copy());
        }

        //返回集合中的最优方案
        return pathOptions.first();
    }

    /**
     * 向前遍历，添加词元，构造一个无歧义词元组合
     *
     * @param lexemeCell path
     * @param option     候选结果路径
     * @return Stack&lt;QuickSortSetLinkedCell&gt;
     */
    private Stack<QuickSortSetLinkedCell> forwardPath(QuickSortSetLinkedCell lexemeCell, LexemePath option) {
        //发生冲突的Lexeme栈
        Stack<QuickSortSetLinkedCell> conflictStack = new Stack<QuickSortSetLinkedCell>();
        QuickSortSetLinkedCell c = lexemeCell;
        //迭代遍历Lexeme链表
        while (c != null && c.getLexeme() != null) {
            if (!option.addNotCrossLexeme(c.getLexeme())) {
                //词元交叉，添加失败则加入lexemeStack栈
                conflictStack.push(c);
            }
            c = c.getNext();
        }
        return conflictStack;
    }

    /**
     * 回滚词元链，直到它能够接受指定的词元
     *
     * @param l      词元
     * @param option 候选结果路径
     */
    private void backPath(Lexeme l, LexemePath option) {
        while (option.checkCross(l)) {
            option.removeTail();
        }
    }
}
