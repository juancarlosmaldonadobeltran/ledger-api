# Ledger API

A simple and lightweight REST API for money transfers between accounts.

## Prerequisites
* [OpenJKD 8](https://adoptopenjdk.net/)

## Dependencies
* [Spark Java](https://github.com/perwendel/spark)
* [Guice](https://github.com/google/guice)
* [SLF4J](https://github.com/qos-ch/slf4j)
* [Gson](https://github.com/google/gson)
* [Lombok](https://projectlombok.org/)
* [JUnit](https://junit.org/junit4/)
* [Mockito](https://site.mockito.org/)
* [Apache HttpComponents Client](https://github.com/apache/httpcomponents-client)
* [Maven](https://maven.apache.org/)
* [Maven Wrapper](https://github.com/takari/maven-wrapper)

## Running the tests

```./mvnw clean install``` 

## Running the application

```./mvnw```

Then navigate to http://localhost:4567/ping in your browser.

## Routes

### 1. Create account

#### Request:


```json
POST /api/accounts
{
  "balance": 10.00
}
```

#### Response:

```json
{
  "status":"SUCCESS",
  "data":{"id": "1e2f6f81-f9b6-4add-8d61-8214d8ae1237",
          "balance": 10.00}
}
```

