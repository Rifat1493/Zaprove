# A loan approval managemnt micro service
- testing junit, mockito, debugging
- database migration
- memory leaks
- spring cloud
- streamline deployment
- docker compose secret for .env
- setters and getters

./mvnw flyway:repair -Dflyway.url=jdbc:postgresql://localhost:5432/mydb -Dflyway.user=myuser -Dflyway.password=mypassword


## Running the Application
Development mode:
```bash
mvn spring-boot:run
```

Production mode:
```bash
mvn clean package
java -jar target/core-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```