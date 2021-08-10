package org.rockkit.poc.resourceserver.service;


import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.model.BookDTO;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookService implements IBookService{


    private BookRepository repo;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.repo = bookRepository;
    }


    @Override
    public List<Book> getAllBooks() {
        return this.repo.findAll();
    }

    @Override
    public BookDTO getBook(Long id) {
        return null;
    }

    @Override
    public void createBook(BookDTO bookDTO) {

    }

    @Override
    public void updateBook(BookDTO bookDTO) {

    }

    @Override
    public void deleteBook(Long id) {

    }
}
