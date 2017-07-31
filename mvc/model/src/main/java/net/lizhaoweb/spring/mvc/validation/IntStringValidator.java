/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : IntValidator.java
 * @Package : net.lizhaoweb.spring.mvc.validation
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月27日
 * @Time : 下午1:07:11
 */
package net.lizhaoweb.spring.mvc.validation;

import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <h1>验证器 - 整形字符串</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public class IntStringValidator implements ConstraintValidator<IntString, String> {

    @Resource
    private ResourceBundleMessageSource messageSource;

    private IntString intString;

    @Override
    public void initialize(IntString intString) {
        this.intString = intString;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
        boolean isValid;
        if (value != null && value.matches("^\\d+$")) {
            isValid = true;
        } else {
            isValid = false;
        }
        if (!isValid) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate(intString.message()).addConstraintViolation();
        }
        return isValid;
    }
}
