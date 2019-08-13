package org.finance_price_service.domain;

public enum AlphaVantageAPIKeywords {
  Meta_Data("Meta Data"),
  Time_Series("Time Series (Daily)"),
  Information("1. Information"),
  Symbol("2. Symbol"),
  Last_Refreshed("3. Last Refreshed"),
  Output_Size("4. Output Size"),
  Time_Zone("5. Time Zone"),
  OPEN("1. open"),
  HIGH("2. high"),
  LOW("3. low"),
  CLOSE("4. close"),
  VOLUME("5. volume"),
  ERROR("Error Message"),
  COMPACT("compact"),
  FULL("full");

  public String key;

  private AlphaVantageAPIKeywords(String key) { this.key = key; }
}
