# Widget Api Assignment - Miro
- --
This project provides to CRUD functionalities via `widget-api`.

https://github.com/folksdev/widget-api

### Summary
A web service to work with widgets via HTTP REST API. The service stores only widgets,
assuming that all clients work with the same board.
### Glossary
A Widget is an object on a plane in a Cartesian coordinate system that has coordinates (X, Y),
Z-index, width, height, last modification date, and a ***unique identifier***. X, Y, and Z-index are
integers (may be negative). Width and height are integers > 0.
Widget attributes should be not null.

A Z-index is a unique sequence common to all widgets that
determines the order of widgets (regardless of their coordinates).
Gaps are allowed. The higher the value, the higher the widget
lies on the plane.

- --
# Widget Model
```sh
String id : PK
Integer x => -2^31 < x < 2^31-1 
Integer y => -2^31 < y < 2^31-1
Integer z => Must be Unique : -2^31 < z < 2^31-1
Integer width =>  0 < width < 2^31-1
Integer height => 0 < height < 2^31-1
Instant lastModificationDate
```

- --
There are two different profiles. One of them provides PostgreSQL database which profile name is `hsql`, the other one provides H2 database which is the `default` profile.


# Technologies
- --
- Spring boot
- Spring Actuator
- Spring Data JPA
- H2 in-memory DB
- HSQLDB in-memory DB 
- Restful API
- SwaggerAPI documentation
- Docker
- Docker compose

# Prerequisites
- ---
- Maven
- Docker

# Run & Build
- --
There are 2 different profiles for widget api application. One is `default` which uses H2 in-memory db. 
Other one is `native` which uses Postgresql as db


There are 3 ways of run & build the widget api application.
### 1. Docker ###
```sh
$PORT 9090         
```
To build and run `widget-api` service via docker

#### For Default Profile ####
```sh 
$ cd widget-api
$ docker build -t widget-api:1.0 .
$ docker run --name widget-api -d -p $PORT:8080 widget-api:1.0
```

#### For HSQL Profile ####
```sh 
$ cd widget-api
$ docker build -t widget-api:1.0 .
$ docker run --name widget-api -d -p $PORT:8080 -e "SPRING_PROFILES_ACTIVE=hsql" widget-api:1.0
```
- --
### 2. Docker-Compose ###
```sh
$PORT 9191       
```
To build and run `widget-api` service via docker-compose

#### For Default Profile ####
```sh
$ cd widget-api
$ docker-compose up
```
#### For HSQL Profile ####
```sh 
$ cd widget-api
$ docker-compose -f docker-compose-hsql.yml up
```
- --
### 2. Maven ###
```sh
$PORT 8080       
```
To build and run `widget-api` service via docker-compose

#### For Default Profile ####
```sh
$ cd widget-api
$ mvn clean install
$ mvn spring-boot:run
```
#### For HSQL Profile ####
```sh 
$ cd widget-api
$ mvn clean install
$ mvn spring-boot:run -Dspring-boot.run.profiles=hsql
```

- --
### Swagger UI will be run on this url
`http://localhost:$PORT/widget-service/swagger-ui.html`
- --
# Usage of the Widget Api
- --
Create Widget : HTTP POST method 

`http://localhost:$PORT/widget-api/widget`

Update Widget: HTTP PUT method

`http://localhost:$PORT/widget-api/widget`

Delete Widget: HTTP DELETE method

`http://localhost:$PORT/widget-api/widget/{widget-id}`

Get a single Widget with a widget id: HTTP GET method

`http://localhost:$PORT/widget-api/widget/{widget-id}`

Get all Widgets with pagination: HTTP GET method (Default page number is 0, size is 10.)

`http://localhost:$PORT/widget-api/widget?page={page-number}&size={size}`


 