## Beanstalk

eb create shop-test -p java --single \
    --envvars SERVER_PORT=5000,SPRING_DATASOURCE_USERNAME=swqsuser,SPRING_DATASOURCE_PASSWORD=swqsswqs,SPRING_DATASOURCE_URL=jdbc:mysql://shop-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com:3306/swqsdemo,SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL5Dialect,SPRING_DATASOURCE_DRIVER-CLASS-NAME=com.mysql.jdbc.Driver

eb deploy

eb terminate shop-test --force

## DB

aws rds create-db-instance --db-name swqsdemo \
    --db-instance-identifier  shop-db --db-instance-class db.t2.micro --engine mysql \
    --allocated-storage 5 --master-username swqsuser --master-user-password swqsswqs

aws rds describe-db-instances

aws rds delete-db-instance --db-instance-identifier shop-db --skip-final-snapshot

mysql -h shop-db.ch2g9nokdg1u.us-west-2.rds.amazonaws.com -u swqsuser -p swqsdemo