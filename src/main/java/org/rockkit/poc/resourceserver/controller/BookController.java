package org.rockkit.poc.resourceserver.controller;

import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.rockkit.poc.resourceserver.model.BookDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(BookController.PATH)
public class BookController {

    protected static final String PATH = "/api/v1/books";

    private final IBookService bookService;

    @Autowired
    public BookController(@Qualifier("BookService")  IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO getBook(@PathVariable Long id) {
        return this.bookService.getBook(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDTO> getBooks() {
        return this.bookService.getAllBooks();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBook(@PathVariable Long id) {
        this.bookService.deleteBook(id);
    }

    //POST is not idempotent
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook( @Valid @RequestBody BookDTO bookDTO) {
        this.bookService.createBook(bookDTO);
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
