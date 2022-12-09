FROM amazoncorretto:17-alpine-jdk as builder

WORKDIR /builder

ENV FLYWAY_DB=catalog
ENV FLYWAY_USER=root
ENV FLYWAY_PASS=root

COPY settings.gradle .
COPY application application
COPY domain domain
COPY infrastructure infrastructure
COPY gradlew gradlew
COPY gradle gradle

RUN ./gradlew build -x test --no-daemon

FROM amazoncorretto:17-alpine

ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=builder /builder/build/libs/application.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "-Xms128m", "-Xmx600m"]
