FROM openjdk:8-jdk-alpine as build
RUN apk add --no-cache maven
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn package -Dmaven.test.skip=true
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# Parameter can be used to change default Sprint Boot port from 8080
# Alternative add --expose=8080 docker argument
EXPOSE 8080 
ENTRYPOINT ["java","-cp","app:app/lib/*","webservice.DhtWebService"]