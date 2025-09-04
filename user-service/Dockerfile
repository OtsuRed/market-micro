# 如果在 CICD 流水线中，项目的打包是由 CICD 服务器自动构建的
# 这里本地演示，所以使用多阶段构建

# 第一阶段：构建
# 一定要看清楚镜像的标签
FROM maven:3.9.11-amazoncorretto-21 AS builder
WORKDIR /app
RUN rm -rf target
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean package -DskipTests
RUN ls -l /app/target/
RUN jar tf /app/target/user-service.jar
RUN jar xf /app/target/user-service.jar META-INF/MANIFEST.MF \
    && cat META-INF/MANIFEST.MF

# 第二阶段：运行
FROM openjdk:21-slim

# 创建日志目录并设置权限（确保目录存在）
RUN mkdir -p /app/logs && \
    mkdir -p /home/nacos/logs && \
    adduser --system --group appuser && \
    chown -R appuser:appuser /app /home/nacos

WORKDIR /app
COPY --from=builder --chown=appuser:appuser /app/target/user-service.jar user-service.jar

USER appuser

EXPOSE 8080

# 强制设置所有相关的系统属性
ENTRYPOINT [ "java", \
             "-Dnacos.home=/app/logs", \
             "-Dnacos.logging.path=/app/logs", \
             "-Dnacos.logging.default.config.enabled=false", \
             "-Dlogging.file.path=/app/logs", \
             "-jar", \
             "user-service.jar" ]