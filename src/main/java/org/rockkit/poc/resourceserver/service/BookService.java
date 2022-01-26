package org.rockkit.poc.resourceserver.service;


import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.exception.BookAlreadyExistsException;
import org.rockkit.poc.resourceserver.filter.BookFilter;
import org.rockkit.poc.resourceserver.model.*;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.rockkit.poc.resourceserver.filter.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Service("BookService")
public class BookService implements IBookService{


    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepo = bookRepository;
    }



    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = this.bookRepo.findAll();
        if (! books.isEmpty())
            return books.stream().map(BookModelMapper::convertEntityToDTO).collect(Collectors.toList());
        else
            throw new BookNotFoundException("No books found");
    }


    @Override
    public Page<BookDTO> getAllBooks(Pageable page) {
        Page<Book> books = this.bookRepo.findAll(page);
        if (! books.isEmpty())
            return books.map(BookModelMapper::convertEntityToDTO);
        else
            throw new BookNotFoundException("No books found");
    }

    @Override
    public Page<BookDTO> getAllBooks(Set<BookFilter> filters, Pageable page) {
        Specification bookSpecification = new BookSpecification(filters);
        Page<Book> books = this.bookRepo.findAll(bookSpecification, page);
        if (! books.isEmpty())
            return books.map(BookModelMapper::convertEntityToDTO);
        else
            throw new BookNotFoundException("No books found");
    }

    @Override
    public List<BookDTO> getAllBooks(Set<BookFilter> filters) {
        Specification bookSpecification = new BookSpecification(filters);
        List<Book> books = this.bookRepo.findAll(bookSpecification);
        if (! books.isEmpty())
            return books.stream().map(BookModelMapper::convertEntityToDTO).collect(Collectors.toList());
        else
            throw new BookNotFoundException("No books found");
    }


    @Override
    public BookDTO getBook(Long id) {
        Optional<Book> book = this.bookRepo.findById(id);
        if (book.isPresent())
            return BookModelMapper.convertEntityToDTO(book.get());
        else
            throw new BookNotFoundException("This book does not exist");

    }


    @Override
    public void createBook(BookDTO bookDTO) {
        try {
            Book book = BookModelMapper.convertDTOToEntity(bookDTO);
            this.bookRepo.save(book);
        }

        //throw exception if book already exists
        catch (DataIntegrityViolationException e) {
            throw new BookAlreadyExistsException("Book already exists");
        }
    }

    @Override
    public void updateBook(BookDTO bookDTO) {
        Book book = BookModelMapper.convertDTOToEntity(bookDTO);
        this.bookRepo.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        try {
            this.bookRepo.deleteById(id);
        }

        //trying to delete a book that does not exist
        catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException("This book does not exist");
        }
    }



}
