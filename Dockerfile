FROM openjdk:17

COPY . .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/Application-0.0.1-SNAPSHOT.jar"]


