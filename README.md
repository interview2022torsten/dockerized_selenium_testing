# Dockerized Selenium Testing
<hr>
This project is just a way of sharing information and might be removed at any given time!
<hr>

## Table of Contents
* [General Info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## <div id="general-info">General Info</div>
This is an example project using Selenium with a dockerized Selenium grid to verify page hyperlinks
<hr>

## <div id="technologies">Technologies</div>
For version details (if not specified) see `pom.xml` file.
* Java
  * openjdk 17.0.1 2021-10-19
  * OpenJDK Runtime Environment (build 17.0.1+12-39)
  * OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing)
* Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
* Apache Maven Surefire
* Apache Maven Failsafe
* JUnit 
* Mockito
* TestNg
* Selenium
<hr>

## <div id="setup">Setup</div>
Make sure you have the specified Java and Maven version running on your machine.

To run the test, first clone the application into your project folder
```
$ git clone git@github.com:interview2022torsten/dockerized_selenium_testing.git
$ cd into <dockerized_selenium_testing>
```

The next step would be to start up the dockerized selenium-grid (might take a while to pull all resources depending on your bandwidth).

```
$ docker-compose --file docker-compose.yaml up
```

To build the project run

```
$ mvn clean test
```

or

```
$ mvn clean compile
```

if one wants to skip the unit tests.


The integration tests can be now run by executing

```
$ mvn failsafe:integration-test
```

or 

```
$ mvn verify
```

if the unit tests should be executed as well.


The test output can be found under 
* Unit tests `/target/surefire-reports/`
  
  <i>Note</i>: Please run `mvn surefire-report:report-only` to generate a HTML report.

* Integration tests `/target/failsafe-reports/`


