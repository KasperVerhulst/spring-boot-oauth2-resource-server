package org.rockkit.poc.resourceserver.model;


import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.Set;

@Getter
@Setter
public class BookDTO {

    private String title;
    private String author;
    private String publisher;
    private Set<String> genres;
    private int release;
    private Long isbn;
    
}
