package org.rockkit.poc.resourceserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.Range;
import org.rockkit.poc.resourceserver.annotation.ISBNConstraint;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
//omit fields in JSON when null
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {

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
