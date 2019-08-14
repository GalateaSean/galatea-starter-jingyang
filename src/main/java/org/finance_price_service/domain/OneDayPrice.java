package org.finance_price_service.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.finance_price_service.domain.OneDayPrice.OneDayPriceId;

@Entity
@Table(name = "stock_prices")
@IdClass(OneDayPriceId.class)
@Builder
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
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

}
