package org.finance_price_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.finance_price_service.domain.AlphaVantageAPIKeywords.*;

/**
 * Deals with the processing logic for upcoming queries.
 */
@Service
public class QueryLogicService {
  @Autowired
  private MySQLService sql;
  @Autowired
  private AlphaVantageService alpha;

  @Value("${date.format}")
  private String dateFormat;

  private boolean marketClosed(String date) throws ParseException {
    Calendar c = Calendar.getInstance();
    c.setTime(new SimpleDateFormat(dateFormat).parse(date));
    if ((c.get(Calendar.DAY_OF_WEEK)) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || sql.isCloseDay(date))
      return true;
    else
      return false;
  }

  /**
   * Querying requests   http://{server_address}/query?symbol=ABCD&days=10
   * @param symbol Stock symbol
   * @param days   Days number that the user requests (starting from today)
   * @return
   */
  public PricesSet query (String symbol, int days) throws Exception {
    PricesSet res = new PricesSet(symbol);
    int countingDays = days;
    boolean updated = false;
    for (int i = 0; i < countingDays; ++i) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -i);
      String date = new SimpleDateFormat(dateFormat).format(cal.getTime());
      OneDayPrice selected = sql.select(symbol, date);
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
        res.addPrice(selected);
      }
    return res;
  }

  /**
   * Updating requests  http://{server_address}/update?symbol=ABCD
   * @param symbol Stock Symbol
   * @return       Single message
   * @throws Exception
   */
  public String update(String symbol, String mode) throws Exception {
    String alphaJsonString = alpha.fetch(symbol, mode);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode alphaJsonNode = mapper.readTree(alphaJsonString);
    if (alphaJsonNode.has(ERROR.key)) return null;

    /**
     * Converts JsonNode to PricesSet
     */
    PricesSet prices = new PricesSet(symbol);
    JsonNode priceArray = alphaJsonNode.get(Time_Series.key);
    Iterator<Entry<String, JsonNode>> fields = priceArray.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> jsonField = fields.next();
      OneDayPrice price = new OneDayPrice();
      String date = jsonField.getKey();
      double open = jsonField.getValue().get(OPEN.key).asDouble();
      double high = jsonField.getValue().get(HIGH.key).asDouble();
      double low = jsonField.getValue().get(LOW.key).asDouble();
      double close = jsonField.getValue().get(CLOSE.key).asDouble();
      int volume = jsonField.getValue().get(VOLUME.key).asInt();
      price.setValue(symbol, date, open, high, low, close, volume);
      prices.addPrice(price);
    }

    sql.insert(prices);
    return String.format("Updated %s data successfully!", symbol);
  }
}