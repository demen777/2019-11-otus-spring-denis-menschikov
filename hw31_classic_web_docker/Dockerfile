FROM adoptopenjdk:11-jre-hotspot

ENV APP_DIR=/opt/books
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY target/books-0.0.1-SNAPSHOT.jar books-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "books-0.0.1-SNAPSHOT.jar"]