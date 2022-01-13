package org.rockkit.poc.resourceserver.model;


import org.rockkit.poc.resourceserver.controller.BookController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<BookDTO, EntityModel<BookDTO>> {

    @Override
    public EntityModel<BookDTO> toModel(BookDTO book) {
        long id = book.getId();
        return EntityModel.of(book,
                linkTo(methodOn(BookController.class).getBook(id)).withSelfRel());
    }

    public EntityModel<BookDTO> toExtendedModel(BookDTO book) {
        long id = book.getId();
        return EntityModel.of(book,
                linkTo(methodOn(BookController.class).getBook(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).getBooks(null)).withRel("GET all"),
                linkTo(methodOn(BookController.class).addBook(null)).withRel("CREATE"),
                linkTo(methodOn(BookController.class).updateBook(null,id)).withRel("UPDATE"),
                linkTo(methodOn(BookController.class).deleteBook(id)).withRel("DELETE"));
    }

    @Override
    public CollectionModel<EntityModel<BookDTO>> toCollectionModel(Iterable<? extends BookDTO> books) {
        //need to cast Generic Iterable to ArrayList
        List<EntityModel<BookDTO>> booksWithLinks = new ArrayList<>();
        for (BookDTO book : books) {
            //add self link to each book
            booksWithLinks.add(EntityModel.of(book.add(linkTo(methodOn(BookController.class).getBook(book.getId())).withSelfRel())));
        }

        return CollectionModel.of(booksWithLinks,linkTo(methodOn(BookController.class).getBooks(null)).withSelfRel());
    }

}
