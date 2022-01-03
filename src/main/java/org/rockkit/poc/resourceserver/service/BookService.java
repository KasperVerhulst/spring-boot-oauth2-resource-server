package org.rockkit.poc.resourceserver.service;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.rockkit.poc.resourceserver.exception.BookNotFoundException;
import org.rockkit.poc.resourceserver.exception.BookAlreadyExistsException;
import org.rockkit.poc.resourceserver.model.Author;
import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.model.BookDTO;
import org.rockkit.poc.resourceserver.repository.AuthorRepository;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Service("BookService")
public class BookService implements IBookService{


    private BookRepository bookRepo;
    private AuthorRepository authorRepo;
    private ModelMapper mapper;


    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.mapper = new ModelMapper();
        mapper.createTypeMap(BookDTO.class, Book.class)
                .addMapping(BookDTO::getRelease,Book::setReleaseYear)
                .addMappings(
                        new PropertyMap<BookDTO, Book>() {
                            @Override
                            protected void configure() {
                                using(ctx -> LocalDateTime.now())
                                        .map(source, destination.getUpdatedAt());
                            }
                        });

        this.bookRepo = bookRepository;
        this.authorRepo = authorRepository;
    }


    @Override
    public List<BookDTO> getAllBooks() {
         List<Book> books = this.bookRepo.findAll();
         if (! books.isEmpty())
             return books.stream().map(b -> convertEntityToDTO(b)).collect(Collectors.toList());
         else
             throw new BookNotFoundException("No books found");
    }

    @Override
    public BookDTO getBook(Long id) {
        Optional<Book> book = this.bookRepo.findById(id);
        if (book.isPresent())
            return this.convertEntityToDTO(book.get());
        else
            throw new BookNotFoundException("This book does not exist");

    }


    @Override
    public void createBook(BookDTO bookDTO) {

        try {
            Book book = convertDTOToEntity(bookDTO);
            this.bookRepo.save(book);
        }

        //throw exception if book already exists
        catch (DataIntegrityViolationException e) {
            throw new BookAlreadyExistsException("Book already exists");
        }
    }

    @Override
    public void updateBook(BookDTO bookDTO) {
        Book book = convertDTOToEntity(bookDTO);
        this.bookRepo.save(book);
    }

    @Override
    public void partialUpdate(Map<String, Object> payload, BookDTO bookDTO) {
        Book book = this.bookRepo.findById(bookDTO.getId()).orElseThrow(BookNotFoundException::new);
        for (String field : payload.keySet()) {
            switch (field.toLowerCase()) {
                case "title":
                    book.setTitle((String) payload.get(field));
                    break;

                case "isbn":
                    book.setIsbn(new Long((Integer) payload.get(field)));
                    break;

                case "publisher":
                    book.setPublisher((String) payload.get(field));
                    break;

                case "author":
                    book.setAuthor(new Author((String) payload.get(field)));
                    break;

                case "release":
                    book.setReleaseYear((Integer) payload.get(field));
                    break;

                case "genres":
                    book.setGenres(Set.copyOf((List<String>) payload.get(field)));
                    break;

                //unknown JSON field to update
                default:

            }
        }
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


    private BookDTO convertEntityToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }

    private Book convertDTOToEntity(BookDTO bookDTO) {
        // here the Book entity is created with a "fresh" Author
        // and no creation date
        Book book = mapper.map(bookDTO, Book.class);

        if (bookDTO.getId() != null) {
            book.setCreatedAt(this.bookRepo.getById(bookDTO.getId()).getCreatedAt());
        }
        else
            book.setCreatedAt(LocalDateTime.now());

        //look if there already exists and author with that name
        Author author = authorRepo.findByFirstNameAndLastName(bookDTO.getAuthor().getFirstName(),bookDTO.getAuthor().getLastName());

        //if the author already exists, add the author to this book
        if ( author != null) {
            book.setAuthor(author);
        }

        return book;
    }


}
