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

The ModelMapper library allows you to quickly convert between Entity and DTO without writing too much boilerplate code.
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
### Pagination and Sorting

### Filtering
Spring Data JPA provides the *JpaRepository* interface that extends the *PagingAndSortingRepository* interface even further. 
This interface allows you to create Query methods that Spring  will automatically transform into the related SQL query. For instance, you can create
queries to search for books with a specific title or genres or books that were released before a certain year:

``` 
List<Book> findAllByTitle(String title)

List<Book> findAllByReleaseYearLessThan(int releaseYear)

List<Book> findAllByGenreIn(List<String> genres)
```

In theory, you could even create more complex queries:

```
List<Book> findAllByTitleAndPublisherAndReleaseyearBetween(String title, String publisher, int bottom, int top)
```
The problem with this approach is that you need to explicitly define every possible query method. Clearly, the number of methods explodes
when the number of fields increases. If we would like to add another field to the Entity than we possibly have to introduce a whole lot of query methods to make all kind
of new queries possible. Furthermore the names of the methods become very long and cumbersome. All these reason make clear that
this strategy is only affordable when there is a very limited set of queries. We would like a more general approach where it is easy to add new queries.

Here is where Criteria API, Predicates, and Specification make their appearance. Specifications form an atomic predicate that you can combine
dynamically to build complex queries.
When you make repository interface extend the *JpaSpecificationExecutor* interface, you can pass a Specification along with the Pageable to the findAll() method.
``` 
public BookSpecifications {
    public static Specification<Book> belongsToGenre(String genre){
      return (root, query, criteriaBuilder)-> 
          criteriaBuilder.in(root.get(Book_.GENRES)).value(genre);
    }
    
    public static Specification<Book> isRecentBook() {
      return (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get(Book.RELEASE_YEAR), LocalDate.now().minusYears(5).getYear());
    }  
}
```
Specifications make it possible to create custom predicates that are not possible with the standard query methods.
Atomic Specifications are loosely coupled and can be dynamically combined with and(), or(), where()... 
```
bookRepository.findAll(where(belongsToGenre()).and(isFromThisyear(new Date().getYear())));
```

Still we would need to predefine every possible filter the client can submit because in practice, you often
 don’t know beforehand what combination of properties the client is going to use to filter the products. That 's why our final strategy is to
 construct a filter based on the query params. This filter is then used to dynamically create a Specification. This allows queries like:
 
```
http://localhost:8080/api/v1/books?publisher=Prometheus
http://localhost:8080/api/v1/books?isbn=!9789491618475
http://localhost:8080/api/v1/books?releaseYear=-2000
http://localhost:8080/api/v1/books?releaseYear=+2000
http://localhost:8080/api/v1/books?releaseYear=2014&author=Dimitri Verhulst
```
### HATEOAS
Hypermedia as the Engine of Application State (HATEOAS)