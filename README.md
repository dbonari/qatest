# qatest

This project is an automation framework skeleton.

Libraries used: 
    `Rest-Assured`
    `maven surefire`
    `jacoco`

`ApiTest.java`: simple jUnit tests which use rest-assured's jsonPath()

`ModelsTest.java`: tests use POJOs from `com.github.dbonari.qatest.model` package

In order to run tests: `mvn clean test`

Test results and coverage reports can be found [here](https://app.circleci.com/pipelines/github/dbonari/qatest?branch=main)

Example of [coverage report](https://27-335686552-gh.circle-artifacts.com/0/Coverage%20report/index.html)