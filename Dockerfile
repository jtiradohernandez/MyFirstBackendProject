FROM openjdk:17-jdk as BUILD
RUN mkdir /app
COPY xaviapi.com-0.0.1.jar /app/xaviapi.com-0.0.1.jar
WORKDIR /app
EXPOSE 8080


ENTRYPOINT ["java","-jar","xaviapi.com-0.0.1.jar"]
