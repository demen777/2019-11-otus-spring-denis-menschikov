FROM maven:3.6.3-jdk-11-slim AS MAVEN_STEP

ENV MVN=/usr/bin/mvn

ENV PROJECT_DIR=/tmp/books
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD pom.xml $PROJECT_DIR
RUN $MVN dependency:resolve

ADD src/ $PROJECT_DIR/src
RUN $MVN package

FROM adoptopenjdk:11-jre-hotspot

ENV APP_DIR=/opt/books
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY --from=MAVEN_STEP /tmp/books/target/books.jar $APP_DIR/

EXPOSE 8080

CMD ["java", "-jar", "books.jar"]