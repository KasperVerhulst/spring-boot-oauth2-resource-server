package org.rockkit.poc.resourceserver.service;

import org.rockkit.poc.resourceserver.model.Book;
import org.springframework.stereotype.Service;
import org.rockkit.poc.resourceserver.model.BookDTO;

import java.util.List;

@Service
public interface IBookService {

    public List<Book> getAllBooks();
    public BookDTO getBook(Long id);

    public void createBook(BookDTO bookDTO);

    public void updateBook(BookDTO bookDTO);

    public void deleteBook(Long id);

}
