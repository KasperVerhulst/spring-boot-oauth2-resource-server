package org.rockkit.poc.resourceserver.controller;

import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.rockkit.poc.resourceserver.model.BookDTO;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addBook(@RequestBody BookDTO book) {

    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateBook(@RequestBody BookDTO book, @PathVariable Long id) {

    }
}
