package org.galatea.jingyang.finance_price_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice;
import org.galatea.jingyang.finance_price_service.domain.PricesSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.galatea.jingyang.finance_price_service.domain.AlphaVantageAPIKeywords.*;

/**
 * Deals with the processing logic for upcoming queries.
 */
@Service
public class QueryLogicService {

  @Autowired
  private MySQLService mySQLService;

  @Autowired
  private AlphaVantageService alphaVantageService;

  @Value("${date.format}")
  private String dateFormat;

  @Value("${message.update-succeed}")
  private String updateSucceedMessage;

  private boolean marketClosed(String date) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new SimpleDateFormat(dateFormat).parse(date));
    return ((cal.get(Calendar.DAY_OF_WEEK)) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || mySQLService.isCloseDay(date));
  }

  /**
   * Querying requests   http://{server_address}/query?symbol=ABCD&days=10
   * @param symbol Stock symbol
   * @param days   Days number that the user requests (starting from today)
   * @return
   */
  public PricesSet query(String symbol, int days) throws Exception {
    PricesSet resPricesSet = PricesSet.builder().symbol(symbol).build();
    int countingDays = days;
    boolean updated = false;
    for (int i = 0; i < countingDays; ++i) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -i);
      String date = new SimpleDateFormat(dateFormat).format(cal.getTime());
      OneDayPrice selected = mySQLService.select(symbol, date);
      if (selected == null) {
        if (!marketClosed(date) && !updated) {
          String mode = days <= 100 ? COMPACT.key : FULL.key;
          if (update(symbol, mode) == null) return null;
          updated = true;
          --i;
        }
        else
          ++countingDays;
      }
      else
        resPricesSet.addPrice(selected);
      }
    return resPricesSet;
  }

  /**
   * Updating requests  http://{server_address}/update?symbol=ABCD
   * @param symbol Stock Symbol
   * @return       Single message
   * @throws Exception
   */
  public String update(String symbol, String mode) throws Exception {
    String alphaJsonString = alphaVantageService.fetch(symbol, mode);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode alphaJsonNode = objectMapper.readTree(alphaJsonString);
    if (alphaJsonNode.has(ERROR.key))
      return null;

    //Converts JsonNode to PricesSet
    PricesSet prices = PricesSet.builder().symbol(symbol).build();
    JsonNode priceArray = alphaJsonNode.get(TIME_SERIES.key);
    Iterator<Entry<String, JsonNode>> fields = priceArray.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> jsonField = fields.next();
      String date = jsonField.getKey();
      double open = jsonField.getValue().get(OPEN.key).asDouble();
      double high = jsonField.getValue().get(HIGH.key).asDouble();
      double low = jsonField.getValue().get(LOW.key).asDouble();
      double close = jsonField.getValue().get(CLOSE.key).asDouble();
      int volume = jsonField.getValue().get(VOLUME.key).asInt();
      OneDayPrice price = OneDayPrice.builder()
          .symbol(symbol)
          .date(date)
          .open(open)
          .high(high)
          .low(low)
          .close(close)
          .volume(volume)
          .build();
      prices.addPrice(price);
    }

    mySQLService.insert(prices);
    return String.format(updateSucceedMessage, symbol);
  }

}