package org.rockkit.poc.resourceserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Getter
@Setter
public class BookDTO {

    @JsonIgnore
    private Long id;

    private String title;
    private Author author;
    private String publisher;
    private Set<String> genres;
    private int release;
    private Long isbn;
    
}
