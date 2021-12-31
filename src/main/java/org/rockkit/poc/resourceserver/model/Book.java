package org.rockkit.poc.resourceserver.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.Set;


@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn( name="AUTHOR_FIRSTNAME", referencedColumnName="firstName")
    @JoinColumn( name="AUTHOR_LASTNAME", referencedColumnName="lastName")
    private Author author;

    private String publisher;

    @ElementCollection
    private Set<String> genres;

    private int releaseYear;

    @Column(unique=true)
    private Long isbn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}
