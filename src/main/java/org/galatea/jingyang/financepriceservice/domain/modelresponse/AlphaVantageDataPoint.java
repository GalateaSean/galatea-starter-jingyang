package org.galatea.jingyang.financepriceservice.domain.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import static org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageAPIKeywords.*;
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
