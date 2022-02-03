package org.rockkit.poc.resourceserver.controller;

import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.filter.BookFilter;
import org.rockkit.poc.resourceserver.filter.BookFilterOperation;
import org.rockkit.poc.resourceserver.model.BookModelAssembler;
import org.rockkit.poc.resourceserver.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.rockkit.poc.resourceserver.model.BookDTO;

import javax.validation.Valid;
import java.util.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(BookController.PATH)
public class BookController {

    //API versioning
    protected static final String PATH = "/api/v1/books";

    //allowed query params for which we allow smaller than or greater than
    private static final Set<String> NUMERICAL_QUERY_PARAMS = new HashSet<String>(Arrays.asList(new String[]{"releaseYear"}));

    //allowed query params
    private static final Set<String> ALL_QUERY_PARAMS = new HashSet<String>(Arrays.asList(new String[]{"title", "author", "publisher",  "releaseYear", "isbn"}));

    //
    private static final Set<String> SET_QUERY_PARAMS = new HashSet<String>(Arrays.asList(new String[]{"genres"}));

    private final IBookService bookService;

    private final BookModelAssembler bookModelAssembler;

    @Autowired
    private PagedResourcesAssembler pagedBookAssembler;


    @Autowired
    public BookController(@Qualifier("BookService")  IBookService bookService, BookModelAssembler bookModelAssembler) {
        this.bookService = bookService;
        this.bookModelAssembler = bookModelAssembler;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<BookDTO>> getBook(@PathVariable Long id) {
        BookDTO book = this.bookService.getBook(id);
        return ResponseEntity.ok().body(bookModelAssembler.toExtendedModel(book));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> getBooks(
            @SortDefault(sort = "title", direction = Sort.Direction.ASC, caseSensitive = false) Pageable page,
            @RequestParam Map<String,String> allRequestParams)
    {
        Set<BookFilter> filters = new HashSet<>();

        for (String param : allRequestParams.keySet()) {
            BookFilter filter = parseRequestParam(param,allRequestParams.get(param));
            if (filter != null)
                filters.add(filter);
        }

        Page<BookDTO> books = this.bookService.getAllBooks(filters, page);
        return ResponseEntity.ok().body(pagedBookAssembler.toModel(books,bookModelAssembler));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        this.bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    //POST is not idempotent
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addBook( @Valid @RequestBody BookDTO bookDTO) {
        this.bookService.createBook(bookDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //PUT must be idempotent
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long id) {
        try {
            this.bookService.getBook(id);
            bookDTO.setId(id);
            this.bookService.updateBook(bookDTO);
            return ResponseEntity.ok().build();
        }
        catch (BookNotFoundException e) {
            this.bookService.createBook(bookDTO);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity patchBook(@RequestBody BookDTO bookDTO, @PathVariable Long id) {
        bookDTO.setId(id);
        this.bookService.updateBook(bookDTO);
        return ResponseEntity.ok().build();
    }

    private BookFilter parseRequestParam(String param, String queryValue) {

        //must be a numerical
        if (queryValue.startsWith("+") && NUMERICAL_QUERY_PARAMS.contains(param)) {
            String value =  queryValue.substring(1);
            return BookFilter.builder().field(param).operator(BookFilterOperation.GREATER_THAN).values(value).build();
        }

        else if (queryValue.startsWith("-") && NUMERICAL_QUERY_PARAMS.contains(param)) {
            String value =  queryValue.substring(1);
            return BookFilter.builder().field(param).operator(BookFilterOperation.SMALLER_THAN).values(value).build();
        }

        else if (queryValue.startsWith("!") && ALL_QUERY_PARAMS.contains(param)) {
            String value =  queryValue.substring(1);
            return BookFilter.builder().field(param).operator(BookFilterOperation.NOT_EQUAL).values(value).build();
        }

        else if(SET_QUERY_PARAMS.contains(param)) {
            return BookFilter.builder().field(param).operator(BookFilterOperation.IN).values(queryValue).build();
        }

        else if (ALL_QUERY_PARAMS.contains(param)) {
            return BookFilter.builder().field(param).operator(BookFilterOperation.EQUAL).values(queryValue).build();
        }

        return null;
    }
}
