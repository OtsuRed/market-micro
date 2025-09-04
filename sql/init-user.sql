CREATE DATABASE IF NOT EXISTS user_service CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE user_service;

CREATE TABLE IF NOT EXISTS `a_user` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(30) NOT NULL,
    `pwd` varchar(20) NOT NULL,
    `tel` char(11) NOT NULL,
    `follow` int unsigned NOT NULL DEFAULT '0',
    `fans` int unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `a_user_unique` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 8 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表';

CREATE TABLE IF NOT EXISTS `user_follow` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `followee_id` int NOT NULL,
    `follower_id` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注表';

CREATE TABLE IF NOT EXISTS `user_pic` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id` int NOT NULL,
    `picture` longblob,
    `picture_narrow` longblob,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户头像表';

CREATE TABLE IF NOT EXISTS `user_profile` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id` int NOT NULL,
    `email` varchar(300) DEFAULT NULL,
    `gender` varchar(5) DEFAULT '',
    `prof` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户简介表';

