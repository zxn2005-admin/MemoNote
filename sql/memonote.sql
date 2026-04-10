-- 个人备忘录系统数据库脚本
-- MySQL 8.0
-- 创建时间: 2024

CREATE DATABASE IF NOT EXISTS memonote DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE memonote;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密存储)',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像',
    `role` VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色: admin-管理员, user-普通用户',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_name` (`user_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 备忘录表
CREATE TABLE IF NOT EXISTS `memo` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '备忘录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `category_id` BIGINT COMMENT '分类ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    `is_complete` TINYINT(1) DEFAULT 0 COMMENT '是否完成: 0-否, 1-是',
    `complete_time` DATETIME COMMENT '完成时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_create_time` (`create_time`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='备忘录表';

-- 初始化管理员账号 密码: admin123 / demo123
INSERT INTO `user` (`username`, `password`, `email`, `role`, `avatar`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@memonote.com', 'admin', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=simple%20minimalist%20user%20avatar%20blue%20color&image_size=square'),
('demo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'demo@memonote.com', 'user', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=simple%20minimalist%20user%20avatar%20green%20color&image_size=square');

-- 初始化演示分类
INSERT INTO `category` (`user_id`, `name`, `color`, `sort`) VALUES
(2, '工作', '#409EFF', 1),
(2, '生活', '#67C23A', 2),
(2, '学习', '#E6A23C', 3),
(2, '其他', '#909399', 4);

-- 初始化演示备忘录
INSERT INTO `memo` (`user_id`, `category_id`, `title`, `content`, `is_top`) VALUES
(2, 1, '完成项目开发', '本周需要完成备忘录系统的全部开发工作，包括前后端联调和测试。', 1),
(2, 2, '购买生活用品', '周末需要去超市购买：牛奶、面包、水果、洗衣液', 0),
(2, 3, '学习Vue3', '学习Vue3的Composition API、响应式原理、组件通信等知识点', 0),
(2, 4, '备忘录系统使用说明', '这是一个简洁的个人备忘录系统，可以记录和管理您的所有备忘。', 0);
