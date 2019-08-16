package org.galatea.jingyang.finance_price_service.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice.OneDayPriceId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OneDayPriceId implements  Serializable {
    private String symbol;
    private String date;
  }

}
