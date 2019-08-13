package org.finance_price_service.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.finance_price_service.domain.OneDayPrice.OneDayPriceId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "stock_prices")
@IdClass(OneDayPriceId.class)
public class OneDayPrice{

  @Id
  @Column(length = 5)
  private String symbol;

  @Id
  @Column(length = 11)
  private String date;

  private double open;
  private double high;
  private double low;
  private double close;
  private int volume;

  public static class OneDayPriceId implements  Serializable {
    private String symbol;
    private String date;
  }

  public void setValue(String symbol, String date, double open, double high, double low, double close, int volume) {
    this.symbol = symbol;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  public String getSymbol() { return this.symbol; }

  public String getDate() { return this.date; }

  public double getOpen() { return this.open; }

  public double getHigh() { return this.high; }

  public double getLow() { return this.low; }

  public double getClose() { return this.close; }

  public int getVolume() { return this.volume; }
}
