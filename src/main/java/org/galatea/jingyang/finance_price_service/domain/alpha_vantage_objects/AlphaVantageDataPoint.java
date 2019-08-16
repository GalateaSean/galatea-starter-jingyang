package org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import static org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects.AlphaVantageAPIKeywords.*;
import lombok.Data;

@Data
public class AlphaVantageDataPoint {

  @JsonProperty (OPEN)
  private double open;

  @JsonProperty (HIGH)
  private double high;

  @JsonProperty (LOW)
  private double low;

  @JsonProperty (CLOSE)
  private double close;

  @JsonProperty (VOLUME)
  private int volume;

}
