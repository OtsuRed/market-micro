#!/bin/bash

if [ ! -f /var/lib/mysql/user_initialized.flag ]; then
    echo "首次启动，执行初始化..."
    mysql -u root -p$MYSQL_ROOT_PASSWORD < /docker-entrypoint-initdb.d/init-user.sql
    touch /var/lib/mysql/user_initialized.flag
else
    echo "数据库已初始化，跳过脚本执行"
fi