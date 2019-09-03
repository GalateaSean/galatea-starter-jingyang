package org.galatea.jingyang.financepriceservice.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.galatea.jingyang.financepriceservice.testutils.TestDataGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

public class PricesSetTest {

  private static Validator validator;

  @BeforeClass
  public static void setUp() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  public void validPricesSet() {
    ArrayList<OneDayPrice> oneDayPriceList = TestDataGenerator.oneDayPriceList(1);
    PricesSet pricesSet = PricesSet.builder()
        .symbol("MSFT")
        .days(1)
        .prices(oneDayPriceList)
        .build();

    Set<ConstraintViolation<PricesSet>> constraintViolations = validator.validate(pricesSet);

    assertEquals(0, constraintViolations.size());
  }

}