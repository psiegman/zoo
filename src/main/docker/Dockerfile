FROM openjdk:11-slim

WORKDIR /opt/app

VOLUME /opt/conf

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

COPY @project.artifactId@-@project.version@.jar zoo.jar

CMD ["java", "-Dspring.config.location=/opt/conf/", "-Djava.security.egd=file:/dev/./urandom", "-jar", "zoo.jar", "-Dserver.contextPath", "/", "-Dserver.port", "8080"]


