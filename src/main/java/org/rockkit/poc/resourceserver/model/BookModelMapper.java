package org.rockkit.poc.resourceserver.model;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.rockkit.poc.resourceserver.repository.AuthorRepository;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookModelMapper {

    private static ModelMapper mapper;

    private static AuthorRepository authorRepo;
    private static BookRepository bookRepo;

    @Autowired
    public BookModelMapper(AuthorRepository authorRepo, BookRepository bookRepo) {
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
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }


    public static BookDTO convertEntityToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }

    public static Book convertDTOToEntity(BookDTO bookDTO) {
        // here the Book entity is created with a "fresh" Author
        // and no creation date
        Book book = mapper.map(bookDTO, Book.class);

        if (bookDTO.getId() != null) {
            book.setCreatedAt(bookRepo.getById(bookDTO.getId()).getCreatedAt());
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


        //TODO: alternative implementation
        //get oldEntity with id from DTO
        //create new Entity from oldEntity and then set every non-null value from DTO
    }
}
