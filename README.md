# Censorship Media Filter (POC)

Proof of concept of a simple filter rest service for complying with censorship policy.

### Building the project

To build the project and execute the whole suite, issue the following command from the project path:

```
$ mvn clean verify
```

### Runing the server

To start the server, use following command from the project path:

```
$ mvn spring-boot:run
```

The server will be ready in http://localhost/media

### Technologies Reference List

 * Spring Boot2
 * Apache Maven3 (surefire and failsafe plugins)
 * JUnit5 + Mockito2
 * JaxB - Jackson
 * Wiremock
 * Log4j2 + Slf4j
 * Lombok
 * Google Guava
 * Hibernate Validation (JSR-303)




