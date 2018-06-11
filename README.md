# BuildingProject
Project for managing data about Building and Units.
## Modules:
* Common - saving common files for the all modules;
* DAO - module for working with database;
* Service - intermediate module between DAO and Rest modules. include calculation, fetching data to the Rest module;
* Rest - module for handling error of request and handling request;
* Web - user interface for interactions with project.

### Build with:
* [Spring Boot](https://spring.io/projects/spring-boot) - for running DAO and Rest modules, testing application;
* [Spring Data](https://spring.io/projects/spring-data) - for accesing to the database;
* [Swagger](https://swagger.io/) - for testing sending request and getting response;
* [Liquibase](https://www.liquibase.org/) - for setting changes to the database;
* [Angular 5](https://angular.io/) - front-end framework for interaction with users;
* [Maven](https://maven.apache.org/) - dependency management;
* [HSQLDB](http://hsqldb.org/) - built-in database for saving and testing.
