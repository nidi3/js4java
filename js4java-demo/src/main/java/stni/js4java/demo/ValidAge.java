package stni.js4java.demo;

import shared.AgeConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 */
@Documented
@Constraint(validatedBy = AgeConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAge {

    String message() default "{validate.age}";

    Class[] groups() default {};

    Class[] payload() default {};
}