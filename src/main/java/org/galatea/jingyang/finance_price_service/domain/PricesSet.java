package org.galatea.jingyang.finance_price_service.domain;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PricesSet {

  private final String symbol;

  @Builder.Default
  private int days = 0;

  @Builder.Default
  private ArrayList<OneDayPrice> prices = new ArrayList<>();

  /**
   * Add a OneDayPrice object to the list
   * @param oneDayPrice A OneDayPrice object
   */
  public void addPrice(OneDayPrice oneDayPrice) {
    ++this.days;
    this.prices.add(oneDayPrice);
  }

}
