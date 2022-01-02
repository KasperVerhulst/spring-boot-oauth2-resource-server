package org.rockkit.poc.resourceserver.model;


import lombok.Getter;
import lombok.Setter;
import org.rockkit.poc.resourceserver.annotation.ISBNConstraint;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import java.util.Set;


@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;


    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn( name="AUTHOR_FIRSTNAME", referencedColumnName="firstName")
    @JoinColumn( name="AUTHOR_LASTNAME", referencedColumnName="lastName")
    @Valid
    private Author author;

    private String publisher;

    @ElementCollection
    private Set<String> genres;

    private int releaseYear;

    @Column(unique=true)
    @ISBNConstraint
    private Long isbn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}
