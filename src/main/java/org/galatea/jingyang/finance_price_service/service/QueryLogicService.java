package org.galatea.jingyang.finance_price_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

  // Date format we are using ("yyyy-MM-dd")
  @Value("${date.format}")
  private String dateFormat;

  // Update process succeed message
  @Value("${message.update-succeed}")
  private String updateSucceedMessage;

  // Threshold number of data points between Alpha Vantage "compact" mode and "full" mode
  @Value("${alphavantage.compact-mode-data-points}")
  private int compactModeDataPoints;

  private boolean marketClosed(String date) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new SimpleDateFormat(dateFormat).parse(date));
    return (cal.get(Calendar.DAY_OF_WEEK)) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || mySQLService.isCloseDay(date);
  }


  private ArrayList<String> getOpenDatesList(int days) throws ParseException {
    ArrayList<String> openDatesList = new ArrayList<>();
    // Days number should be greater than 0
    if (days <= 0) return openDatesList;
    // Number of days counting back from yesterday (possible to be market closed date)
    int countDays = 1;
    // Number of market open days ever met
    int openDatesNumber = 0;
    while (openDatesNumber < days) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE, -countDays);
      String countingDate = new SimpleDateFormat(dateFormat).format(calendar.getTime());
      if (!marketClosed(countingDate)) {
        openDatesList.add(countingDate);
        ++openDatesNumber;
      }
      ++countDays;
    }
    assert openDatesList.size() == days;
    return openDatesList;
  }

  private ArrayList<String> getDatesToUpdate(ArrayList<OneDayPrice> pricesList, ArrayList<String> openDatesList) {
    ArrayList<String> datesInDB = new ArrayList<>();
    for (OneDayPrice price : pricesList) {
      datesInDB.add(price.getDate());
    }
    openDatesList.removeAll(datesInDB);
    return openDatesList;
  }

  private String updatePrices(String symbol, int days, List<String> datesToUpdate) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String mode = days <= compactModeDataPoints ? COMPACT : FULL;
    String alphaJsonString = alphaVantageService.fetch(symbol, mode);
    JsonNode alphaJsonNode = objectMapper.readTree(alphaJsonString);
    if (alphaJsonNode.has(ERROR)) return null;
    JsonNode timeSeries = alphaJsonNode.get(TIME_SERIES);
    for (String date : datesToUpdate) {
      OneDayPrice price = OneDayPrice.builder()
          .symbol(symbol)
          .date(date)
          .open(timeSeries.get(date).get(OPEN).asDouble())
          .high(timeSeries.get(date).get(HIGH).asDouble())
          .low(timeSeries.get(date).get(LOW).asDouble())
          .close(timeSeries.get(date).get(CLOSE).asDouble())
          .volume(timeSeries.get(date).get(VOLUME).asInt())
          .build();
      mySQLService.insertSinglePrice(price);
    }
    return String.format(updateSucceedMessage, symbol);
  }

  /**
   * Querying requests   http://{server_address}/query?symbol=ABCD&days=10
   * @param symbol Stock symbol
   * @param days   Days number that the user requests (starting from today)
   * @return
   */
  public PricesSet queryPrices(String symbol, int days) throws ParseException, IOException {
    ArrayList<String> openDatesList = getOpenDatesList(days);
    // The first date in the result price data, i.e. the furthest date we want
    String startDate = openDatesList.get(openDatesList.size() - 1);
    // The last date in the result price data, i.e. yesterday date
    String endDate = openDatesList.get(0);
    ArrayList<OneDayPrice> priceList = mySQLService.selectPrices(symbol, startDate, endDate);
    if (priceList.size() < days) {
      ArrayList<String> datesToUpdate = getDatesToUpdate(priceList, openDatesList);
      if (updatePrices(symbol, days, datesToUpdate) == null) return null;
      priceList = mySQLService.selectPrices(symbol, startDate, endDate);
    }
    return PricesSet.builder().symbol(symbol).days(days).prices(priceList).build();
  }

}