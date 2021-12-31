package org.rockkit.poc.resourceserver.repository;

import org.rockkit.poc.resourceserver.model.Author;
import org.rockkit.poc.resourceserver.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByAuthor(Author author);

}
