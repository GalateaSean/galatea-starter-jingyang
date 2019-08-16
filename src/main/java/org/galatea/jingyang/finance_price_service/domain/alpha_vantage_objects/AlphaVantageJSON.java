package org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice;
import org.galatea.jingyang.finance_price_service.domain.PricesSet;
import static org.galatea.jingyang.finance_price_service.domain.alpha_vantage_objects.AlphaVantageAPIKeywords.*;

@Data
public class AlphaVantageJSON {

  @JsonProperty (META_DATA)
  private AlphaVantageMetaData metaData;

  @JsonProperty (TIME_SERIES)
  private Map<String, AlphaVantageDataPoint> timeSeries;

  @JsonProperty (ERROR)
  private String errorMessage;


  /**
   * Gets a PricesSet of data points to be inserted into database
   *
   * @param datesToUpdate A list of dates on which price data is to be inserted
   * @return  PricesSet
   */
  public PricesSet getPricesToUpdate(List<String> datesToUpdate) {
    if (this.errorMessage != null) return null;
    ArrayList<OneDayPrice> pricesList = new ArrayList<>();
    // Add each date to be inserted into prices list
    for (String date : datesToUpdate) {
      AlphaVantageDataPoint alphaVantageDataPoint = this.timeSeries.get(date);
      OneDayPrice oneDayPrice = OneDayPrice.builder()
          .symbol(this.metaData.getSymbol())
          .date(date)
          .open(alphaVantageDataPoint.getOpen())
          .high(alphaVantageDataPoint.getHigh())
          .low(alphaVantageDataPoint.getLow())
          .close(alphaVantageDataPoint.getClose())
          .volume(alphaVantageDataPoint.getVolume())
          .build();
      pricesList.add(oneDayPrice);
    }
    return PricesSet.builder()
        .symbol(this.metaData.getSymbol())
        .days(pricesList.size())
        .prices(pricesList)
        .build();
  }

}
