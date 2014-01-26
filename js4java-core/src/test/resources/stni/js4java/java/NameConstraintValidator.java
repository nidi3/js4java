package stni.js4java.java;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import stni.js4java.java.Validators;

public class NameConstraintValidator implements ConstraintValidator<Annotation, String>{

  @Autowired
  private Validators validators;

  public void initialize(Annotation constraintAnnotation) {}

  /**
   * bla
   * @param name w
   * @return ret
   */
  public boolean isValid(String name, ConstraintValidatorContext context){
    return validators.isValidName(name);
  }

}