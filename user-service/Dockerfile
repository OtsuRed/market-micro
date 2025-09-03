# 如果在 CICD 流水线中，项目的打包是由 CICD 服务器自动构建的
# 这里本地演示，所以使用多阶段构建

# 第一阶段：构建
# 一定要看清楚镜像的标签
FROM maven:3.9.11-amazoncorretto-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行
FROM openjdk:21-slim
WORKDIR /app
RUN adduser --system --group appuser && \
    chown -R appuser:appuser /app
# --from=builder 允许从上一阶段的容器内复制文件
# 如果缺失，则 COPY 命令会在宿主机上寻找文件
COPY --from=builder --chown=appuser:appuser /app/target/user-service.jar user-service.jar

USER appuser

RUN jar tf user-service.jar > /dev/null && echo "JAR文件验证成功" || echo "JAR文件损坏"

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "user-service.jar" ]