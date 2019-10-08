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

Status code: 201 Created

```json
{
  "status": "SUCCESS",
  "data": {
           "id": "8fd7029e-8bda-48cd-b325-f7d9f610b5a8",
           "balance": 10.00
          }
}
```

#### Headers:
Location : /api/accounts/8fd7029e-8bda-48cd-b325-f7d9f610b5a8


### 2. Get account by id

#### Request:

```json
GET /accounts/8fd7029e-8bda-48cd-b325-f7d9f610b5a8
```

#### Response

Status code: 200 OK

```json
{  
 "status": "SUCCESS",
 "data": {
          "id": "8fd7029e-8bda-48cd-b325-f7d9f610b5a8",
          "balance" :10.00
         }
}
```

#### Possible errors

Status code: 404 NotFound

```json
{
 "status": "ERROR", 
 "message":"Account not found"
}
```

### 3. Get all accounts

#### Request:

```json
GET /accounts
```

#### Response:

Status code: 200 OK
```json
{
 "status": "SUCCESS",
 "data":[
         {"id": "8fd7029e-8bda-48cd-b325-f7d9f610b5a8",
          "balance": 10.00
         },
         {
          "id": "d7634ad9-2c54-4578-815d-c958799d3042",
          "balance": 5.00
         }
        ]
}
```

