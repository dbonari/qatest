# qatest

This project is an automation framework skeleton.

Libraries used: `Rest-Assured`, `maven surefire`, `jacoco`.

- `ApiTest.java`: simple jUnit tests that use rest-assured's jsonPath().

- `ModelsTest.java`: tests use POJOs from `com.github.dbonari.qatest.model` package.

In order to run the tests execute: `mvn clean test`.

Test results and coverage reports can be found [here](https://app.circleci.com/pipelines/github/dbonari/qatest?branch=main).

Example of a [coverage report](https://output.circle-artifacts.com/output/job/ab24dd83-d4f0-411e-803a-71e65966bb57/artifacts/0/Coverage%20report/index.html).
