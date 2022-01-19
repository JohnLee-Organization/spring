/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 08:16
 */
package net.lizhaoweb.spring.mvc.uas.model;

import lombok.Getter;

/**
 * <H1>模型 [枚举] - 加密方式</H1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月04日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public enum EncryptionAlgorithm {
    MD2("MD2"),
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),
    UNKNOWN("");

    @Getter
    private String name;

    private EncryptionAlgorithm(String name) {
        this.name = name;
    }

    public static EncryptionAlgorithm fromName(String name) {
        try {
            for (EncryptionAlgorithm algorithm : values()) {
                if (algorithm.getName().equalsIgnoreCase(name)) {
                    return algorithm;
                }
            }
            return UNKNOWN;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
