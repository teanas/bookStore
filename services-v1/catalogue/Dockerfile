FROM openjdk:11.0.8
ARG APP_FILE=target/*.jar   
EXPOSE 8082
COPY $APP_FILE app.jar  
ENTRYPOINT ["java","-jar","/app.jar"]  