package org.rockkit.poc.resourceserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Author implements  Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

//    @OneToMany(targetEntity = Book.class, mappedBy = "author")
//    private Set<Book> books;

}
