package org.rockkit.poc.resourceserver.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

    private String title;
    private String author;
    private String editor;
    private int year;
    private int isbn;
    
}
