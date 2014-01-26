package stni.js4java.demo;

import shared.HeightConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 */
@Documented
@Constraint(validatedBy = HeightConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHeight {

    String message() default "{validate.height}";

    Class[] groups() default {};

    Class[] payload() default {};
}