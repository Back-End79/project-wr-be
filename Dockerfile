FROM harbor.cloudias79.com/devops-tools/openjdk:11-jre-slim
COPY target/wrcore-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]