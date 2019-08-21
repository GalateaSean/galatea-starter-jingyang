package org.galatea.jingyang.financepriceservice.domain.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice;
import org.galatea.jingyang.financepriceservice.domain.PricesSet;
import static org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageAPIKeywords.*;

@Data
public class AlphaVantageJSON {

  @JsonProperty (META_DATA)
  private AlphaVantageMetaData metaData;

  @JsonProperty (TIME_SERIES)
  private Map<String, AlphaVantageDataPoint> timeSeries;

  @JsonProperty (ERROR)
  private String errorMessage;

}
