package org.rockkit.poc.resourceserver.repository;

import org.rockkit.poc.resourceserver.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByFirstNameAndLastName(String firstName, String lastName);

}
