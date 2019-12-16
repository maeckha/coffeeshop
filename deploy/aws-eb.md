## Test Environment
####################

# create

eb create CoffeeShop-env-test -p java --single \
    --envvars SERVER_PORT=5000,SPRING_DATASOURCE_USERNAME=admin,SPRING_DATASOURCE_PASSWORD=_test42,SPRING_DATASOURCE_URL=jdbc:mysql://coffeeshop-test-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com:3306/coffeeshop,SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL5Dialect,SPRING_DATASOURCE_DRIVER-CLASS-NAME=com.mysql.jdbc.Driver

aws rds create-db-instance --db-name coffeeshop \
    --db-instance-identifier  coffeeshop-test-db --db-instance-class db.t2.micro --engine mysql \
    --allocated-storage 5 --master-username admin --master-user-password _test42

# operate

mysql -h shop-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com -u swqsuser -p swqsdemo

http://shop-test.8qvzgknme3.us-west-2.elasticbeanstalk.com

eb deploy

aws rds describe-db-instances

# destroy

eb terminate CoffeeShop-env-test --force

aws rds delete-db-instance --db-instance-identifier shop-db --skip-final-snapshot



## Staging Environment
#######################

# create


aws rds create-db-instance --db-name coffeeshop \
    --db-instance-identifier  coffeeshop-staging-db --db-instance-class db.t2.micro --engine mysql \
    --allocated-storage 5 --master-username admin --master-user-password stage_42

eb create CoffeeShop-env-staging -p java --single \
    --envvars SERVER_PORT=5000,SPRING_DATASOURCE_USERNAME=admin,SPRING_DATASOURCE_PASSWORD=stage_42,SPRING_DATASOURCE_URL=jdbc:mysql://coffeeshop-staging-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com:3306/coffeeshop,SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL5Dialect,SPRING_DATASOURCE_DRIVER-CLASS-NAME=com.mysql.jdbc.Driver

# destroy

aws rds delete-db-instance --db-instance-identifier coffeeshop-staging-db --skip-final-snapshot

eb terminate CoffeeShop-env-staging --force





## Production Environment
##########################


eb create CoffeeShop-env-production -p java --single \
    --envvars SERVER_PORT=5000,SPRING_DATASOURCE_USERNAME=swqsuser,SPRING_DATASOURCE_PASSWORD=swqsswqs,SPRING_DATASOURCE_URL=jdbc:mysql://shop-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com:3306/swqsdemo,SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL5Dialect,SPRING_DATASOURCE_DRIVER-CLASS-NAME=com.mysql.jdbc.Driver

