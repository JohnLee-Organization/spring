USE sys;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `district`
-- ----------------------------
DROP TABLE IF EXISTS `district`;
CREATE TABLE `district` (
    `id` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '自增主键',
    `name` VARCHAR(45) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '名称',
    `short_name` VARCHAR(45) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '简称',
    `full_name` VARCHAR(254) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '全称',
    `english_name` VARCHAR(254) DEFAULT NULL COMMENT '英文名称',
    `pinyin` VARCHAR(100) DEFAULT NULL COMMENT '拼音',
    `short_pinyin` VARCHAR (50) DEFAULT NULL COMMENT '简拼',
    `parent_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父标识',
    `type` TINYINT(4) UNSIGNED DEFAULT NULL COMMENT '类型。0是国，1是省，2是市，3是区、县',
    `hierarchy` TINYINT(4) UNSIGNED DEFAULT NULL COMMENT '所处的层级',
    `district_code` VARCHAR(12) DEFAULT NULL COMMENT '行政编码',
    `post_code` VARCHAR(6) DEFAULT NULL COMMENT '邮政编码',
    `area_codes` VARCHAR(4) DEFAULT NULL COMMENT '区号',
    `lng` VARCHAR(45) DEFAULT NULL COMMENT 'GCJ02坐标系经度',
    `lat` VARCHAR(45) DEFAULT NULL COMMENT 'GCJ02坐标系纬度',
    `memo` VARCHAR(254) DEFAULT NULL COMMENT '备注',
    `state` SMALLINT(6) UNSIGNED DEFAULT 0x01 COMMENT '状态。0x00：删除；0x01：正常',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of `district`
-- ----------------------------
INSERT INTO `district` VALUES (1, '中国', '中国', '中华人民共和国', 'the People\'s Republic of China', 'ZhongGuo', 'ZG', 0, 0, 1, '', '', '+86', '', '', '', 0x01, '2021-08-31 17:42:00', '2021-08-31 17:43:00');