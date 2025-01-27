# 使用 Maven 官方镜像作为基础镜像进行编译
FROM maven:3.9.9-amazoncorretto-23-alpine AS build
# 设置工作目录
WORKDIR /app
# 复制项目的 pom.xml 文件到工作目录
COPY pom.xml .
# 下载项目依赖
RUN mvn dependency:go-offline
# 复制项目源代码到工作目录
COPY src ./src
# 将资源文件复制到容器中
COPY src/main/resources/application.properties .
# 执行 Maven 打包命令
RUN mvn clean package -D skipTests -P prod

# 使用 Tomcat 官方镜像作为运行环境
FROM tomcat:latest
# 删除 Tomcat 默认的示例应用
RUN rm -rf /usr/local/tomcat/webapps/*
# 从前面的编译阶段复制打包好的 WAR 文件到 Tomcat 的 webapps 目录
COPY --from=build /app/target/oa-1.0.war /usr/local/tomcat/webapps/ROOT.war
# 暴露 Tomcat 的默认端口 8080
EXPOSE 8080
# 启动 Tomcat 服务器
CMD ["catalina.sh", "run"]