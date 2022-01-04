package org.rockkit.poc.resourceserver.service;

import org.springframework.data.domain.Pageable;
import org.rockkit.poc.resourceserver.model.BookDTO;

import java.util.List;


public interface IBookService {

    public List<BookDTO> getAllBooks(Pageable page);
    public BookDTO getBook(Long id);

    public void createBook(BookDTO bookDTO);

    public void updateBook(BookDTO bookDTO);

    public void deleteBook(Long id);

}
