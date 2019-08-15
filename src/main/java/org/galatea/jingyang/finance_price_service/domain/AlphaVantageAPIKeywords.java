package org.galatea.jingyang.finance_price_service.domain;

public enum AlphaVantageAPIKeywords {
  META_DATA("Meta Data"),
  TIME_SERIES("Time Series (Daily)"),
  INFORMATION("1. Information"),
  SYMBOL("2. Symbol"),
  LAST_REFRESHED("3. Last Refreshed"),
  OUTPUT_SIZE("4. Output Size"),
  TIME_ZONE("5. Time Zone"),
  OPEN("1. open"),
  HIGH("2. high"),
  LOW("3. low"),
  CLOSE("4. close"),
  VOLUME("5. volume"),
  ERROR("Error Message"),
  COMPACT("compact"),
  FULL("full");

  public final String key;

  private AlphaVantageAPIKeywords(String key) { this.key = key; }
}
