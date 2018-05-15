[![pipeline status](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/pipeline.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)
[![coverage report](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/coverage.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)

# swqs-shop-ui
The shop ui depends on the shoppingcart, catalog and order module. 
It delivers a serverbased Web-Application, realized with [Spring Boot](https://projects.spring.io/spring-boot/) 
and [Thymeleaf](https://www.thymeleaf.org/).

The shopping cart of the customer is managed serverside by a simple list, 
the client holds the cart id in a cookie.

## deployment

First you must install the three following dependencies in your [local maven repository](http://www.baeldung.com/maven-local-repository) 
with the `mvn install` command.
* swqs-catalog
* swqs-cart
* swqs-order-management

Alternatively, you can download the final jar file from the ci builds and store them manually in your local maven repository. That's the way we co for the ci tests and build.

## selenium tests
To run the selenium test you need a gecko driver for your os in the root folder of the project and the firefox browser installed. Download the driver from: https://github.com/mozilla/geckodriver/releases

