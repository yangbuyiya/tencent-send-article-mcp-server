# # 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds

LABEL maintainer="Yang buyiya <yangbuyiya@duck.com>"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY target/tencent-send-article-mcp-server-app.jar /tencent-send-article-mcp-server-app.jar

ENV SERVER_PORT=8633 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

SHELL ["/bin/bash", "-c"]

ENTRYPOINT java -Dspring.profiles.active=sse \
  -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
  -jar /tencent-send-article-mcp-server-app.jar