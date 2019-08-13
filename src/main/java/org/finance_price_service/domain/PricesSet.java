package org.finance_price_service.domain;

import java.util.Vector;

public class PricesSet {
  private final String symbol;
  private int days;
  private Vector<OneDayPrice> prices;

  public PricesSet(String symbol) {
    this.symbol = symbol;
    this.days = 0;
    this.prices = new Vector<OneDayPrice>();
  }

  public PricesSet(String symbol, int days, Vector<OneDayPrice> prices) {
    this.symbol = symbol;
    this.days = days;
    this.prices = prices;
  }

  public void addPrice(OneDayPrice p) {
    ++this.days;
    this.prices.add(p);
  }

  public String getSymbol() {
    return this.symbol;
  }

  public int getDays() {
    return this.days;
  }

  public Vector<OneDayPrice> getPrices() {
    return this.prices;
  }
}
