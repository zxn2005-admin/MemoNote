-- =============================================
-- 备忘录系统数据库初始化脚本
-- 数据库: MySQL 8.0
-- 创建时间: 2024
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS memonote DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE memonote;

-- =============================================
-- 用户表
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role` TINYINT DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 分类表
-- =============================================
DROP TABLE IF EXISTS `memo_category`;
CREATE TABLE `memo_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- =============================================
-- 备忘录表
-- =============================================
DROP TABLE IF EXISTS `memo_note`;
CREATE TABLE `memo_note` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '备忘录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `priority` TINYINT DEFAULT 0 COMMENT '优先级: 0-普通, 1-重要, 2-紧急',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未完成, 1-已完成',
    `reminder_time` DATETIME DEFAULT NULL COMMENT '提醒时间',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON数组)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_reminder_time` (`reminder_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='备忘录表';

-- =============================================
-- 操作日志表
-- =============================================
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `operation` VARCHAR(100) DEFAULT NULL COMMENT '操作描述',
    `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =============================================
-- 创建视图
-- =============================================

-- 备忘录统计视图
DROP VIEW IF EXISTS `v_memo_stats`;
CREATE VIEW `v_memo_stats` AS
SELECT 
    u.id AS user_id,
    u.username,
    COUNT(DISTINCT n.id) AS total_notes,
    SUM(CASE WHEN n.status = 0 THEN 1 ELSE 0 END) AS pending_notes,
    SUM(CASE WHEN n.status = 1 THEN 1 ELSE 0 END) AS completed_notes,
    COUNT(DISTINCT c.id) AS total_categories
FROM sys_user u
LEFT JOIN memo_note n ON u.id = n.user_id AND n.deleted = 0
LEFT JOIN memo_category c ON u.id = c.user_id AND c.deleted = 0
WHERE u.deleted = 0
GROUP BY u.id, u.username;

SELECT '数据库初始化完成!' AS message;
SELECT '注意: 管理员账号将在应用启动时自动创建 (用户名: admin, 密码: admin123)' AS notice;
