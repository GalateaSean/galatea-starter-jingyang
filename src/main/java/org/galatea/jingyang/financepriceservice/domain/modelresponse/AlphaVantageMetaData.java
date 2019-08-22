package org.galatea.jingyang.financepriceservice.domain.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import static org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageAPIKeywords.*;
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
