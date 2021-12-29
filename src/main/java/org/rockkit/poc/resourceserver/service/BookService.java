package org.rockkit.poc.resourceserver.service;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.rockkit.poc.resourceserver.model.Book;
import org.rockkit.poc.resourceserver.model.BookDTO;
import org.rockkit.poc.resourceserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("BookService")
public class BookService implements IBookService{


    private BookRepository repo;
    private ModelMapper mapper;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.mapper = new ModelMapper();

        //customize ModelMapper for Author mapping
        mapper.createTypeMap(Book.class, BookDTO.class)
                .addMappings(
                        new PropertyMap<Book, BookDTO>() {
                            @Override
                            protected void configure() {
                                using(ctx -> ((Book) ctx.getSource()).getAuthor().getFirstName() + " " + ((Book) ctx.getSource()).getAuthor().getLastName())
                                        .map(source, destination.getAuthor());
                            }
                        });
        this.repo = bookRepository;
    }


    @Override
    public List<BookDTO> getAllBooks() {
         List<Book> books = this.repo.findAll();
         return books.stream().map(x -> convertEntityToDTO(x)).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBook(Long id) {
         return this.convertEntityToDTO(this.repo.findById(id).orElse(null));

    }

    @Override
    public void createBook(BookDTO bookDTO) {
        Book book = mapper.map(bookDTO, Book.class);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        this.repo.save(book);
    }

    @Override
    public void updateBook(BookDTO bookDTO) {
        Book book = convertDTOToEntity(bookDTO);
        book.setUpdatedAt(LocalDateTime.now());
        this.repo.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        this.repo.deleteById(id);
    }


    private BookDTO convertEntityToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }

    private Book convertDTOToEntity(BookDTO bookDTO) {
        return mapper.map(bookDTO, Book.class);
    }
}
