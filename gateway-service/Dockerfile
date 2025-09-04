# 构建
FROM maven:3.9.11-amazoncorretto-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN rm -rf target
RUN mvn clean package -DskipTests

# 第二阶段：运行
FROM openjdk:21-slim

# 创建日志目录并设置权限（确保目录存在）
RUN mkdir -p /app/logs && \
    mkdir -p /home/nacos/logs && \
    adduser --system --group appuser && \
    chown -R appuser:appuser /app /home/nacos

WORKDIR /app
COPY --from=builder --chown=appuser:appuser /app/target/gateway-service.jar gateway-service.jar

USER appuser

EXPOSE 8080

# 强制设置所有相关的系统属性
ENTRYPOINT [ "java", \
             "-Dnacos.home=/app/logs", \
             "-Dnacos.logging.path=/app/logs", \
             "-Dnacos.logging.default.config.enabled=false", \
             "-Dlogging.file.path=/app/logs", \
             "-jar", \
             "gateway-service.jar" ]