#!/bin/bash

if [ ! -f /var/lib/mysql/productinitialized.flag ]; then
    echo "首次启动，执行初始化..."
    mysql -u root -p$MYSQL_ROOT_PASSWORD < /docker-entrypoint-initdb.d/init-product.sql
    touch /var/lib/mysql/product_initialized.flag
else
    echo "数据库已初始化，跳过脚本执行"
fi