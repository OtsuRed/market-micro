# 构建
FROM maven:3.9.11-amazoncorretto-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN rm -rf target
RUN mvn clean package -DskipTests

# 运行
FROM openjdk:21-slim
WORKDIR /app
RUN adduser --system --group appuser && \
    chown -R appuser:appuser /app
COPY --from=builder --chown=appuser:appuser /app/target/gateway-service.jar gateway-service.jar

USER appuser

RUN jar tf product-service.jar > /dev/null && echo "JAR文件验证成功" || echo "JAR文件损坏"

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "gateway-service.jar" ]