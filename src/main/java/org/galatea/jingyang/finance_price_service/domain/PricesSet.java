package org.galatea.jingyang.finance_price_service.domain;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PricesSet {

  private final String symbol;

  private int days;

  private ArrayList<OneDayPrice> prices;

}
