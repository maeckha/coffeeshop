# How to Deploy on Heroku

## Deploy

Login to heroku:
```
> heroku login
```
Then create heroku appplication with the name **cryptorich**:

```
> heroku create coffeeshop
```

```
> git push heroku master 
```
```
> heroku config:set spring.datasource.url=jdbc:mysql://193.196.52.162/coffeeshop
> heroku config:set spring.datasource.username=coffeeshop
> heroku config:set spring.datasource.password=<PASSWORD>
> heroku config:set spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect


```