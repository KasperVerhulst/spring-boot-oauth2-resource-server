package org.rockkit.poc.resourceserver.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBNConstraint {
    String message() default "ISBN number is missing or invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
