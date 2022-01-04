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
Creating an interface for your business logic is good practice for complexer projects. Although it may not be necessary here, 
it allows again for loose coupling between the Controller and the Service.
The interface defines the functionality, but multiple implementations can exist for different databases. You can inject the 
interface in the controller and in case multiple implementations exist, the tags `@Qualifier` or `@Primary` allows you to select the desired
implementation. With `@Qualifier` you can select the name of a specific Component, with `@Primary` you can define a default implementation that will be
injected when no specific `@Qualifier` is specified.

## REST API standard
### Http Status Codes

* 200 *Ok* : successful get or update
* 201 *Created* : successful creation
* 202 *Accepted* : request was correctly received but not yet processed

* 301 *Moved Permanently* : URL of the requested resource has been changed permanently. The new URL is given by the Location header field in the response.
* 302 *Found* : URL of the requested resource has been changed temporarily. The new URL is given by the Location field in the response.

* 400 *Bad Request* :  incorrect syntax of the request
* 401 *Unauthorized* : client did not bring any authentication
* 403 *Forbidden* : client is authenticated but not authorized
* 404 *Not Found* : resource does not exist
* 405 *Method Not Allowed* : client tried to use an HTTP method that the resource does not allow
* 406 *Not Acceptable* : indicates that the API is not able to generate any of the client’s preferred media types, as indicated by the Accept request header
* 409 *Conflict* : request could not be completed due to a conflict with the current state of the resource
* 410 *Gone* : resource existed before, but is no longer available
### Filter, Pagination and Sorting

### HATEOAS