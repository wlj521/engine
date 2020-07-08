# 基于java镜像创建新镜像
FROM java:8

MAINTAINER wlj
# 将jar包添加到容器中并更名为app.jar
ADD target/engine.jar app.jar

EXPOSE 8080
# 运行jar包
ENTRYPOINT ["java","-jar","./app.jar","&"]