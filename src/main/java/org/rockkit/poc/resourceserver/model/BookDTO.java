package org.rockkit.poc.resourceserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.Range;
import org.rockkit.poc.resourceserver.annotation.ISBNConstraint;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@Relation(collectionRelation = "books")
//omit fields in JSON when null
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO extends RepresentationModel<BookDTO> {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "title cannot be empty")
    private String title;

    @Valid
    private Author author;
    private String publisher;
    private Set<String> genres;

    @Range(min = 1000, max = 2500, message = "This is not a valid year")
    private int release;


    @ISBNConstraint
    private Long isbn;
    
}
