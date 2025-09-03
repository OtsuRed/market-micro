CREATE DATABASE IF NOT EXISTS product_service CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

use product_service;

CREATE TABLE `product` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(50) NOT NULL,
    `price` decimal(10, 2) NOT NULL,
    `amount` int NOT NULL DEFAULT '1',
    `seller_id` int NOT NULL,
    `prof` varchar(500) NOT NULL COMMENT '商品描述',
    `state` int NOT NULL DEFAULT '0' COMMENT '商品状态',
    `want` int NOT NULL DEFAULT '0' COMMENT '想要的人数',
    `seller_name` varchar(50) NOT NULL,
    `create_time` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 10 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表';

CREATE TABLE `product_category` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `product_id` int NOT NULL,
    `category_id` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品种类表';

CREATE TABLE `product_fav` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `pid` int NOT NULL,
    `uid` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 15 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品收藏表';

CREATE TABLE `product_pic` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `product_id` int NOT NULL,
    `kind` int NOT NULL,
    `picture` longblob NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表';
