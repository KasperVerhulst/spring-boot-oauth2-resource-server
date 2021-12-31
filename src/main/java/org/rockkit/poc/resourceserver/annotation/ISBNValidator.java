package org.rockkit.poc.resourceserver.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ISBNValidator implements ConstraintValidator<ISBNConstraint, Long> {

    @Override
    public void initialize(ISBNConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        String isbnRegEx = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})" +
                "[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";
        return value == null ? false :  Pattern.matches(isbnRegEx,value.toString());
    }
}
