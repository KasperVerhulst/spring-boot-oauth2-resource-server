package org.rockkit.poc.resourceserver.filter;


import org.rockkit.poc.resourceserver.model.Author;
import org.rockkit.poc.resourceserver.model.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.Set;


public class BookSpecification implements Specification<Book> {
    
    Set<BookFilter> filters = null;

    public BookSpecification(Set<BookFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        for (BookFilter filter : filters) {
            switch (filter.getOperator()) {
                case EQUAL:
                    return criteriaBuilder.equal(root.get(filter.getField()),
                            castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues()));

                case NOT_EQUAL:
                    return criteriaBuilder.notEqual(root.get(filter.getField()),
                            castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues()));

                case SMALLER_THAN:
                    return criteriaBuilder.lessThan(root.get(filter.getField()), filter.getValues());

                case GREATER_THAN:
                    return criteriaBuilder.greaterThan(root.get(filter.getField()), filter.getValues());

                case IN:
                    return root.join(filter.getField()).in(castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues()));

                default:
                    throw new UnsupportedOperationException("filter operation not supported for this query param");
            }
        }
        return null;
    }

    /**
     *
     * @param fieldType
     * @param value
     * @return
     */
    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Author.class)) {
            return new Author(value);
        }

        else if (fieldType.isAssignableFrom(Number.class)) {
            return Long.parseLong(value);
        }

        else if(fieldType.isAssignableFrom(Set.class)) {
            return Collections.singleton(value);
        }

        else
            return value;
    }

}
