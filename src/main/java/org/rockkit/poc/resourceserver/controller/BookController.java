package org.rockkit.poc.resourceserver.controller;

import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.model.BookModelAssembler;
import org.rockkit.poc.resourceserver.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.rockkit.poc.resourceserver.model.BookDTO;

import javax.print.DocFlavor;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(BookController.PATH)
public class BookController {

    //API versioning
    protected static final String PATH = "/api/v1/books";

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
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> getBooks(@SortDefault(sort = "title", direction = Sort.Direction.ASC, caseSensitive = false) Pageable page) {
        Page<BookDTO> books = this.bookService.getAllBooks(PageRequest.of(page.getPageNumber(),page.getPageSize(),page.getSort()));
        return ResponseEntity.ok().body(pagedBookAssembler.toModel(books,bookModelAssembler));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        this.bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    //POST is not idempotent
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addBook( @Valid @RequestBody BookDTO bookDTO) {
        this.bookService.createBook(bookDTO);
        return ResponseEntity.noContent().build();
    }

    //PUT must be idempotent
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long id) {
        try {
            this.bookService.getBook(id);
            bookDTO.setId(id);
            this.bookService.updateBook(bookDTO);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (BookNotFoundException e) {
            this.bookService.createBook(bookDTO);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }
}
