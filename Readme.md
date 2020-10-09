# Basic Spring Boot REST + React TS

## Requirements

- node 12.18.2, npm 6.14.8
- Java OpenJDK 11.0.8, maven 4
- Docker

## Local development

```bash
docker-compose up
# open http://localhost:9000

cd frontend
npm i
cd ..
mvn spring-boot:run
# open http://localhost:8080

cd frontend
npm start
open http://localhost:3000
```

## Prod build

```bash
mvn clean install
mvn spring-boot:run
```

```bash
mvn package
java -jar target/demo-0.0.1-SNAPSHOT.jar
open http://127.0.0.1:8080
```

## Push to Heroku

```bash
git push heroku master
```
