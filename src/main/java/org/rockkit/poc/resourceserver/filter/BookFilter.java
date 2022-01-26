package org.rockkit.poc.resourceserver.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class BookFilter {

    private String field;
    private BookFilterOperation operator;
    private Set<String> values;


}
