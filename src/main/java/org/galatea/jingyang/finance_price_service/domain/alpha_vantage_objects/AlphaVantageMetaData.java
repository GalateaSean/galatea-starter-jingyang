package org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import static org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects.AlphaVantageAPIKeywords.*;
import lombok.Data;

@Data
public class AlphaVantageMetaData {

  @JsonProperty(INFORMATION)
  private String information;

  @JsonProperty(SYMBOL)
  private String symbol;

  @JsonProperty(LAST_REFRESHED)
  private String lastRefreshed;

  @JsonProperty(OUTPUT_SIZE)
  private String outputSize;

  @JsonProperty(TIME_ZONE)
  private String timeZone;

}
