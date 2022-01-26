package org.rockkit.poc.resourceserver.filter;


import org.rockkit.poc.resourceserver.model.BookDTO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;


public class BookSpecification implements Specification<BookDTO> {
    
    Set<BookFilter> filters = null;

    public BookSpecification(Set<BookFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<BookDTO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        for (BookFilter filter : filters) {
            switch (filter.getOperator()) {
                case EQUAL:
                    return criteriaBuilder.equal(root.get(filter.getField()), filter.getValues());

                default:
                    throw new UnsupportedOperationException("filter operation not supported");
            }
        }
        return null;
    }

}
