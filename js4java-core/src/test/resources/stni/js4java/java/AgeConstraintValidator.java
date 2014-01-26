package stni.js4java.java;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import stni.js4java.java.Validators;

public class AgeConstraintValidator implements ConstraintValidator<Annotation, Double>{

  @Autowired
  private Validators validators;

  public void initialize(Annotation constraintAnnotation) {}

  /**
   * bla
   * @param age w
   * @return ret
   */
  public boolean isValid(Double age, ConstraintValidatorContext context){
    return validators.isValidAge(age);
  }

}