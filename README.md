# swqs-shop-ui
The shop ui depends on the shoppingcart, catalog and order module. It delivers a serverbased Web-Application, realized with [Spring Boot](https://projects.spring.io/spring-boot/) and [Thymeleaf](https://www.thymeleaf.org/).

The shopping cart of the customer is managed serverside by a simple list, the client holds the cart id in a cookie.

## deployment

First you must install the three following dependencies on your local maven repository([?](http://www.baeldung.com/maven-local-repository)). Run 'install' as build goal. 
* swqs-catalog
* swqs-cart
* swqs-order-management

