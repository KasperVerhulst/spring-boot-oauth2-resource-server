# OAuth2 RESTful resource server
This service is essentially just a simple RESTful API. Hence it can be used as an OAuth2 resource server to test a OAuth2 application.

## Spring Boot

### Model vs DTO
Advantages for keeping DTO and model as two separate classes. The model reflects the data as persisted in the database, the DTO reflects the user interface. 
* loose coupling between entity and UI layer
* hide properties from database you don't want to show on view
* avoid overfetching and underfetching
* you can present different object to different frontends that possibly require other properties
* a DTO can group fields from multiple backend stores

### Spring Boot layered architecture
* Controller layer: accept and respond to HTTP requests. Serialization and deserialization of JSON to POJOs
* Service layer: business logic
* Persistence layer: connection with database

### Field Injection vs Constructor Injection
Constructor injection:
```
public class SomeService {
    private final SomeOtherService someOtherService;

    @Autowired
    public SomeService(SomeOtherService someOtherService){
        this.someOtherService = someOtherService;
    }
}
```

Field injection:
```
@Component
public class SomeService {
     @Autowired private SomeOtherService someOtherService;
}
```

Constructor injection is generally preferred:
* You cannot create an instance of SomeService with the default constructor (`new SomeService()`) without actually resolving the dependency.
* dependency can be final, which helps for robustness and thread-safety.
* You don't need reflection to create tests

### Service layer interface
Creating an interface for your business logic is good practice for complexer projects as it allows again for loose coupling. 
The interface defines the functionality, but multiple implementations can exist for different databases. You can inject the 
interface in the controller and in case multiple implementations exist, the tags `@Qualifier` or `@Primary` allows you to select the desired
implementation.

## REST API standard