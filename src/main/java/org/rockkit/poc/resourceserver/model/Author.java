package org.rockkit.poc.resourceserver.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Author implements  Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Author needs a first name")
    private String firstName;

    @NotBlank(message = "Author needs a last name")
    private String lastName;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "author", targetEntity = Book.class)
    private Set<Book> books;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author (String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    //constructor that will be used for deserialization
    @JsonCreator
    public Author(String fullName) {
        String[] name = fullName.split(" ", 2);

        this.firstName = name[0];
        this.lastName = name[1];

    }

    //Method that determines how an Author will be presented in JSON
    @JsonValue
    public String toJSON() {
        return firstName + " " + lastName;
    }
}
