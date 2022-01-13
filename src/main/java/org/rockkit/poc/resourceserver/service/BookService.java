package org.rockkit.poc.resourceserver.service;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.exception.BookAlreadyExistsException;
import org.rockkit.poc.resourceserver.model.Author;
import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.model.BookDTO;
import org.rockkit.poc.resourceserver.model.BookModelMapper;
import org.rockkit.poc.resourceserver.repository.AuthorRepository;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public Page<BookDTO> getAllBooks(Pageable page) {
         Page<Book> books = this.bookRepo.findAll(page);
         if (! books.isEmpty())
             return books.map(BookModelMapper::convertEntityToDTO);
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
