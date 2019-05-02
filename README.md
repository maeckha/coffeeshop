[![pipeline status](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/pipeline.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)
[![coverage report](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/coverage.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)

# swqs-shop-ui
The shop ui delivers a serverbased Web-Application, realized with [Spring Boot](https://projects.spring.io/spring-boot/) 
and [Thymeleaf](https://www.thymeleaf.org/).

The shopping cart of the customer is managed serverside by a simple list, 
the client holds the cart id in a cookie.

## database

The database which is used by the catalog and shoppingcart component can be configured through the [application.properties](src/main/resources/application.properties) file. For the various tests we use a in-memory database.

## deployment

The application can be build with maven, a runnable jar file with all dependencies and a embedded webserver will be created when at least the `package` phase is executed.

## Usage

Just run the created jar file, the spring boot application should start. The default port is 8080. 

## Tests
To run the tests with maven for the application you have to use following build phases: 

* `mvn test` (for unit tests, class name ends with `Test`) or 
* `mvn verify` (for integration tests, class name ends with `IT`)

The Surefire / Failsafe Plugin will pickup the tests automatically when using these naming patterns.

#### selenium tests
To run the selenium test you need a gecko driver for your os in the root folder of the project 
and the firefox browser installed. 

Download the driver from: https://github.com/mozilla/geckodriver/releases

The Selenium tests doesn't run automatically while the maven build process. You have to start them manually. This is done because the Selenium tests won't run in the ci environment easily.

#### cucumber functional tests
There's a minimal example of a cucumber test which checks the functionality of the home controller (with the [spring mvc test framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework])). It's for demonstrating the usage of cucumber inside a spring project. The cucumber tests can be combined with selenium to achieve some functional system tests based on user stories.

#### Mail
We send mails to our fake mail server, you can verify the mail sending when you look to following page: [www.smtpbucket.com](https://www.smtpbucket.com/).
Enter as sender: webshop@swqs.org and as recipient the email address you expect the mail.

## reports
The project includes various maven code analyzing plugins. When running `mvn site` a html site with all reports, test results and the javadoc will be generated into the `target/site` directory.

---

This application was written for my bachelor degree
at the university of applied science Constance.


Mirko Bay, 2018
