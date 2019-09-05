package org.galatea.jingyang.financepriceservice.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClosedDateTest {

  private static Validator validator;

  @BeforeClass
  public static void setUp() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  public void validClosedDate() {
    ClosedDate closedDate = ClosedDate.builder()
        .date("2019-09-03")
        .build();

    Set<ConstraintViolation<ClosedDate>> constraintViolations = validator.validate(closedDate);

    assertEquals(0, constraintViolations.size());
  }

}
