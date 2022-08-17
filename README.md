# Spring Boot,Spring Security JWT Authentication & Authorization

## User Registration, User Login and Authorization process.
The diagram shows flow of how we implement User Registration, User Login and Authorization process.

![Sequence Diagram](https://github.com/messaoud06/AuthCRUDPostgres/blob/main/Registration%20Login%20process.png?raw=true)


## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`

```
spring.datasource.url= jdbc:postgresql://localhost:5432/simpledb
spring.datasource.username= postgres
spring.datasource.password= root

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto= update

# App Properties
jwt.secret=myTesting
jwt.expiration=86400000
```
## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```


