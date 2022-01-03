package org.rockkit.poc.resourceserver.service;

import org.rockkit.poc.resourceserver.model.Book;
import org.springframework.stereotype.Service;
import org.rockkit.poc.resourceserver.model.BookDTO;

import java.util.List;
import java.util.Map;


public interface IBookService {

    public List<BookDTO> getAllBooks();
    public BookDTO getBook(Long id);

    public void createBook(BookDTO bookDTO);

    public void partialUpdate(Map<String,Object> update, BookDTO bookDTO);

    public void updateBook(BookDTO bookDTO);

    public void deleteBook(Long id);

}
