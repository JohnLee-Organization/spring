/**
 * Copyright (c) 2013, 2014, XinZhe and/or its affiliates. All rights reserved.
 * XINZHE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.lizhaoweb.spring.mvc.core.servlet;

import net.lizhaoweb.common.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 生成随机验证码
 *
 * @author bitiliu
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2014-10-23<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public class ValidateCodeServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final class Request {
        public static final class Parameter {
            public static final class Name {
                public static final String IS_REQUEST_RESET = "isRequestReset";
                public static final String IS_REQUEST_RESIZE = "isRequestResize";
                public static final String WIDTH = "width";
                public static final String HEIGHT = "height";
                public static final String CODE_COUNT = "codeCount";
                public static final String LINE_COUNT = "lineCount";
            }
        }
    }

    public static final Pattern REGULAR_EXPRESSION_NUMBER_INTEGER = Pattern.compile("^\\d+$");

    public static final String VALIDATE_CODE_KEY = ValidateCodeServlet.class.getName() + ".VALIDATE_CODE_KEY";

    private boolean requestReset = false;
    private boolean requestResize = false;
    private int width = 60;// 验证码图片的宽度。
    private int height = 20;// 验证码图片的高度。
    private int codeCount = 4;// 验证码字符个数
    private int lineCount = 160;// 干扰线的数量

    private int charWidth = 0;// 字符宽度
    private int charHeight = 0;// 字符高度

    private int fontSize = -1;// 字体大小

    private int codeX = -1;// 每个字符的宽度
    private int codeY = -1;// 每个字符的高度

    private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 初始化验证图片属性
     */
    public void init() throws ServletException {
        // 从web.xml中获取初始信息
        String requestResetString = this.getInitParameter(Request.Parameter.Name.IS_REQUEST_RESET);// 请求是否可以设置
        String requestResizeString = this.getInitParameter(Request.Parameter.Name.IS_REQUEST_RESIZE);// 请求是否设置图片尺寸
        String strWidth = this.getInitParameter(Request.Parameter.Name.WIDTH);// 宽度
        String strHeight = this.getInitParameter(Request.Parameter.Name.HEIGHT);// 高度
        String strCodeCount = this.getInitParameter(Request.Parameter.Name.CODE_COUNT);// 字符个数
        String strLineCount = this.getInitParameter(Request.Parameter.Name.LINE_COUNT);// 干扰线的数量

        // 将配置的信息转换成数值
        try {
            requestReset = "true".equalsIgnoreCase(requestResetString);
            requestResize = "true".equalsIgnoreCase(requestResizeString);
            if (strWidth != null && strWidth.trim().length() > 0 && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strWidth.trim()).find()) {
                width = Integer.parseInt(strWidth.trim());
            }
            if (strHeight != null && strHeight.trim().length() > 0 && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strHeight.trim()).find()) {
                height = Integer.parseInt(strHeight.trim());
            }
            if (strCodeCount != null && strCodeCount.trim().length() > 0 && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strCodeCount.trim()).find()) {
                codeCount = Integer.parseInt(strCodeCount.trim());
            }
            if (strLineCount != null && strLineCount.trim().length() > 0 && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strLineCount.trim()).find()) {
                lineCount = Integer.parseInt(strLineCount.trim());
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void service(HttpServletRequest requst, HttpServletResponse response) throws ServletException, IOException {
        logger.info("service('%s', '%s')", requst, response);

        if (requestResize) {
            // 验证码图片的宽度。
            String strWidth = requst.getParameter(Request.Parameter.Name.WIDTH);// 宽度
            if (StringUtil.isNotBlank(strWidth) && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strWidth.trim()).find()) {
                width = Integer.parseInt(strWidth.trim());
            }
            // 验证码图片的高度。
            String strHeight = requst.getParameter(Request.Parameter.Name.HEIGHT);// 高度
            if (StringUtil.isNotBlank(strHeight) && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strHeight.trim()).find()) {
                height = Integer.parseInt(strHeight.trim());
            }
        }
        if (this.requestReset) {
            // 验证码字符个数
            String strCodeCount = requst.getParameter(Request.Parameter.Name.CODE_COUNT);// 字符个数
            if (StringUtil.isNotBlank(strCodeCount) && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strCodeCount.trim()).find()) {
                codeCount = Integer.parseInt(strCodeCount.trim());
            }
            // 干扰线的数量
            String strLineCount = requst.getParameter(Request.Parameter.Name.LINE_COUNT);// 干扰线的数量
            if (StringUtil.isNotBlank(strLineCount) && REGULAR_EXPRESSION_NUMBER_INTEGER.matcher(strLineCount.trim()).find()) {
                lineCount = Integer.parseInt(strLineCount.trim());
            }
        }

        fontSize = height - 2;

        // 定义图像buffer
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        // 创建一个随机数生成器类
        Random random = new Random();

        // 将图像填充为白色
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontSize);
        // 设置字体。
        graphics2D.setFont(font);

        // 画边框。
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(0, 0, width - 1, height - 1);

        int red = 0, green = 0, blue = 0;

        // 随机产生lineCount条干扰线，使图象中的认证码不易被其它程序探测到。
        for (int index = 0; index < lineCount; index++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xl = random.nextInt(width / 2);
            int yl = random.nextInt(height / 2);

            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            graphics2D.setColor(new Color(red, green, blue));
            graphics2D.drawLine(xs, ys, xs + xl, ys + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        charHeight = height - fontSize / 6;
        codeY = charHeight * 1;

        charWidth = width / (codeCount + 2);
        // 随机产生codeCount数字的验证码。
        for (int index = 0; index < codeCount; index++) {
            codeX = (index + 1) * charWidth;

            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            graphics2D.setColor(new Color(red, green, blue));
            graphics2D.drawString(strRand, codeX, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }

        // 将四位数字的验证码保存到Session中。
        HttpSession session = requst.getSession();
        session.setAttribute(VALIDATE_CODE_KEY, randomCode.toString());

        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", sos);
        sos.close();
    }

    @Override
    protected void initC() {
    }
}
