package org.galatea.jingyang.financepriceservice.domain;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PricesSet {

  private final String symbol;

  private int days;

  private ArrayList<OneDayPrice> prices;

}
