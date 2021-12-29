package org.rockkit.poc.resourceserver.repository;

import org.rockkit.poc.resourceserver.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

}
