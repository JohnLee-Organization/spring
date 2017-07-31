/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 08:11
 */
package net.lizhaoweb.spring.mvc.uas.model;

import lombok.Getter;
import net.lizhaoweb.spring.mvc.uas.util.Constant;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月04日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SystemConfig {

    @Getter
    private boolean takeEffect;

    @Getter
    private boolean checkValidateCode;

    @Getter
    private EncryptionAlgorithm passwordEncryptionAlgorithm;

    @Value(Constant.Application.Custom.Property.SpringKey.TAKE_EFFECT)
    public void setTakeEffect(String takeEffect) {
        this.takeEffect = "TRUE".equalsIgnoreCase(takeEffect);
    }

    @Value(Constant.Application.Custom.Property.SpringKey.CHECK_VALIDATE_CODE)
    public void setCheckValidateCode(String checkValidateCode) {
        this.checkValidateCode = "TRUE".equalsIgnoreCase(checkValidateCode);
    }

    @Value(Constant.Application.Custom.Property.SpringKey.PASSWORD_ENCRYPTION_ALGORITHM)
    public void setPasswordEncryptionAlgorithm(String encryptionAlgorithm) {
        this.passwordEncryptionAlgorithm = EncryptionAlgorithm.fromName(encryptionAlgorithm);
    }
}
