[![pipeline status](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/pipeline.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)
[![coverage report](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/badges/master/coverage.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-shop-ui/commits/master)

# swqs-shop-ui
The shop ui depends on the shoppingcart, catalog and order module. 
It delivers a serverbased Web-Application, realized with [Spring Boot](https://projects.spring.io/spring-boot/) 
and [Thymeleaf](https://www.thymeleaf.org/).

The shopping cart of the customer is managed serverside by a simple list, 
the client holds the cart id in a cookie.

## deployment

First you must install the three following dependencies on your [local maven repository](http://www.baeldung.com/maven-local-repository). Run 'install' as build goal. 
* swqs-catalog
* swqs-cart
* swqs-order-management

## selenium tests
To run the selenium test you need a gecko driver for your os in the root folder of the project. Download them from: https://github.com/mozilla/geckodriver/releases

