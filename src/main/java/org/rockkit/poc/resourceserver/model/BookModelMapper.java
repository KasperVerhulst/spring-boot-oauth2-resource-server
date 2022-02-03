package org.rockkit.poc.resourceserver.model;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.rockkit.poc.resourceserver.repository.AuthorRepository;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

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
        mapper.getConfiguration().setSkipNullEnabled(true);
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }


    public static BookDTO convertEntityToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }

    /**
     * Create an Entity from given DTO. If an Entity already exists for the DTO's id, the
     * existing Entity is updated with all @NonNull values from DTO.
     * @param bookDTO
     * @return
     */
    public static Book convertDTOToEntity(BookDTO bookDTO) {


        //get oldEntity with id from DTO
        if (bookDTO.getId() != null) {
            Optional<Book> book = bookRepo.findById(bookDTO.getId());

            //merge old and new book
            if (book.isPresent()) {
                Book newBook = book.get();
                mapper.map(bookDTO, newBook);

                if (bookDTO.getAuthor() != null)
                    parseAuthor(newBook, bookDTO.getAuthor().getFirstName(), bookDTO.getAuthor().getLastName());

                return newBook;
            }
        }

        // if book with this id not found or there was no id
        // need to create a new book
        Book newBook = mapper.map(bookDTO, Book.class);
        newBook.setCreatedAt(LocalDateTime.now());

        if (bookDTO.getAuthor() != null)
            parseAuthor(newBook, bookDTO.getAuthor().getFirstName(), bookDTO.getAuthor().getLastName());

        return newBook;
    }

    /**
     * Update reference in case author already exists
     * @param book
     * @param firstName
     * @param lastName
     */
    private static void parseAuthor(Book book, String firstName, String lastName) {
        Author author = authorRepo.findByFirstNameAndLastName(firstName, lastName);

        //if the author already exists, add the author to this book
        if (author != null) {
            book.setAuthor(author);
        }

    }
}
