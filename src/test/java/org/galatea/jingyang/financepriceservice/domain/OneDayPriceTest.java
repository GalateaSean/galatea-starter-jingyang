package org.galatea.jingyang.financepriceservice.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class OneDayPriceTest {

  private static Validator validator;

  @BeforeClass
  public static void setUp() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  public void validOneDayPrice() {
    OneDayPrice oneDayPrice = OneDayPrice.builder()
        .symbol("MSFT")
        .date("2019-09-03")
        .open(1)
        .high(2)
        .low(3)
        .close(4)
        .volume(5)
        .build();

    Set<ConstraintViolation<OneDayPrice>> constraintViolations = validator.validate(oneDayPrice);

    assertEquals(0, constraintViolations.size());
  }

}
