package org.rockkit.poc.resourceserver.service;

import org.rockkit.poc.resourceserver.filter.BookFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.rockkit.poc.resourceserver.model.BookDTO;

import java.util.List;
import java.util.Set;


public interface IBookService {

    /**
     * Return all books from the repository
     * @return
     */
    public List<BookDTO> getAllBooks();

    /**
     * Return all books from the repository in the requested pagination format
     * @param page
     * @return
     */
    public Page<BookDTO> getAllBooks(Pageable page);

    /**
     * Return the books that satisfy the provided filters in the requested pagination format
     * @param filters
     * @param page
     * @return
     */
    public Page<BookDTO> getAllBooks(Set<BookFilter> filters, Pageable page);

    /**
     * Return the books that satisfy the provided filters
     * @param filters
     * @return
     */
    public List<BookDTO> getAllBooks(Set<BookFilter> filters);

    public BookDTO getBook(Long id);

    public void createBook(BookDTO bookDTO);

    public void updateBook(BookDTO bookDTO);

    public void deleteBook(Long id);

}
