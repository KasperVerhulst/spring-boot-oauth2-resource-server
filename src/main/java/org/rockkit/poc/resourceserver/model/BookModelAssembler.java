package org.rockkit.poc.resourceserver.model;


import org.rockkit.poc.resourceserver.controller.BookController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<BookDTO, EntityModel<BookDTO>> {

    @Override
    public EntityModel<BookDTO> toModel(BookDTO book) {
        return EntityModel.of(book,
                linkTo(methodOn(BookController.class).getBook(book.getId()))
                        .withSelfRel());
    }
}
