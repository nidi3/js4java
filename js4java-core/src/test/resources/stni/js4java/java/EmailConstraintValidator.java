package stni.js4java.java;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import stni.js4java.java.Validators;

public class EmailConstraintValidator implements ConstraintValidator<Annotation, String>{

  @Autowired
  private Validators validators;

  public void initialize(Annotation constraintAnnotation) {}

  /**
   * bla
   * @param email w
   * @return ret
   */
  public boolean isValid(String email, ConstraintValidatorContext context){
    return validators.isValidEmail(email);
  }

}