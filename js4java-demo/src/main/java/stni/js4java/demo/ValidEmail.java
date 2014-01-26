package stni.js4java.demo;

import shared.EmailConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 */
@Documented
@Constraint(validatedBy = EmailConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "{validate.email}";

    Class[] groups() default {};

    Class[] payload() default {};
}