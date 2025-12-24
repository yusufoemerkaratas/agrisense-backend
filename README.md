# agrisense-backend

Quarkus-based REST backend for managing sensors, measurements, and alert rules. Uses JPA/Hibernate Validator, H2 (test), and simple HATEOAS links on responses.

## Requirements

- Java 21+
- Maven Wrapper (`./mvnw` included)

## How to run

Dev mode (hot reload, Dev UI at http://localhost:8080/q/dev/):

```bash
./mvnw compile quarkus:dev
```

Packaging (JAR):

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

Uber-jar (single runnable):

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

Native (optional, needs GraalVM or containerized build):

```bash
./mvnw package -Dnative                  # local GraalVM
./mvnw package -Dnative -Dquarkus.native.container-build=true  # container
./target/agrisense-backend-1.0.0-SNAPSHOT-runner
```

## Testing

**Run all tests (WITHOUT Docker - for P03 submission):**

```bash
./mvnw verify
```

This command will:
- Run all 122 unit and integration tests
- Build the Quarkus application
- Verify the build is successful

**Build with Docker image (when needed):**

```bash
./mvnw verify -Pdocker
```

This will:
- Run all tests
- Build the application
- **Automatically build Docker image** using Quarkus container-image extension
- Create image: `agrisense-backend:latest` and `agrisense-backend:1.0.0-SNAPSHOT`

**Run Docker container:**

```bash
docker run -i --rm -p 8080:8080 agrisense-backend:latest
```

**Notes for P03:**
- `mvn verify` runs all tests and builds the application
- Docker daemon must be running only if using `-Pdocker` profile
- Tests run: 122, Failures: 0, Errors: 0
- Build time: ~10 seconds
- No manual Docker commands needed - everything automated via Maven

## Default ports

- Dev mode: 8080 (Quarkus default)
- Test profile & packaged app (current config): 8081

## Sample API usage (HTTP)

Base path is `/api` for sensors/measurements, and `/sensors/{sensorId}/rules` for alert rules.

- Create sensor:

```bash
curl -X POST http://localhost:8081/api/sensors \
	-H "Content-Type: application/json" \
	-d '{"name":"SensorA","type":"TEMPERATURE","apiKey":"k1","fieldId":1}'
```

- Get sensor by id:

```bash
curl http://localhost:8081/api/sensors/1
```

- Create alert rule for a sensor:

```bash
curl -X POST http://localhost:8081/sensors/1/rules \
	-H "Content-Type: application/json" \
	-d '{"name":"HighTemp","condition":"GREATER_THAN","threshold":25.0,"description":"warn"}'
```

- Post measurement (triggers rule evaluation):

```bash
curl -X POST http://localhost:8081/api/measurements \
	-H "Content-Type: application/json" \
	-d '{"sensorId":1,"value":30.0,"unit":"C"}'
```

- List alerts with paging/filter:

```bash
curl "http://localhost:8081/api/alerts?page=1&size=10&status=OPEN"
```

## Data and validation notes

- H2 in-memory is used for tests; main runtime DB config is expected via standard Quarkus properties.
- Validation: sensor name/type/apiKey/fieldId required; alert rule name/condition/threshold required; measurement sensorId/value required.
- Error handling: 400 for invalid input, 404 for missing resources.

## Tech stack

- Quarkus 3.29.x, RESTEasy Reactive, Hibernate ORM, Hibernate Validator
- Caffeine cache via `quarkus-cache`
- Testing: JUnit5, RestAssured, Mockito

## Contributors

- Beyza Betül Ay (5123039)
- Yusuf Ömer Karataş (5123015)
- Nihal Beyza Dogan (5123109)
