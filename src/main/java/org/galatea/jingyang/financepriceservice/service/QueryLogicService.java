package org.galatea.jingyang.financepriceservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice;
import org.galatea.jingyang.financepriceservice.domain.PricesSet;
import org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageDataPoint;
import org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageAPIKeywords.*;

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

  /**
   * Check if market is closed on a date
   *
   * @param date Date string, format "yyyy-MM-dd"
   * @return Boolean
   * @throws ParseException
   */
  private boolean marketClosed(String date) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new SimpleDateFormat(dateFormat).parse(date));
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        || mySQLService.isCloseDay(date);
  }

  /**
   * Counts back specific number of days from yesterday for dates wanted
   *
   * @param days Number of days
   * @return     List of all market open dates
   * @throws ParseException
   */
  private ArrayList<String> getOpenDatesList(int days) throws ParseException {
    ArrayList<String> openDatesList = new ArrayList<>();
    // Days number should be greater than 0
    if (days <= 0) {
      return openDatesList;
    }
    // Number of days counting back from yesterday (possible to be market closed date)
    int countDays = 1;
    // Number of market open days ever met
    int openDatesNumber = 0;
    Calendar calendar = Calendar.getInstance();
    while (openDatesNumber < days) {
      calendar.add(Calendar.DATE, -countDays);
      String countingDate = new SimpleDateFormat(dateFormat).format(calendar.getTime());
      calendar.clear();
      if (!marketClosed(countingDate)) {
        openDatesList.add(countingDate);
        ++openDatesNumber;
      }
      ++countDays;
    }
    assert openDatesList.size() == days;
    return openDatesList;
  }

  /**
   * Compare wanted dates data with existing dates in database, get missing dates
   *
   * @param pricesList    List of prices data in database
   * @param openDatesList Wanted open market dates
   * @return Listed of missing dates, to be updated
   */
  private ArrayList<String> getDatesToUpdate(ArrayList<OneDayPrice> pricesList, ArrayList<String> openDatesList) {
    ArrayList<String> datesInDB = new ArrayList<>();
    for (OneDayPrice price : pricesList) {
      datesInDB.add(price.getDate());
    }
    openDatesList.removeAll(datesInDB);
    return openDatesList;
  }

  /**
   * Reach Alpha Vantage API for data, insert wanted missing data into database
   *
   * @param symbol        Stock symbol
   * @param days          Number of dates the user requested
   * @param datesToUpdate List of dates to be inserted into database
   * @return Update succeed message
   * @throws IOException
   */
  private String updatePrices(String symbol, int days, ArrayList<String> datesToUpdate) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String mode = days < compactModeDataPoints ? COMPACT : FULL;
    String alphaVantageJsonString = alphaVantageService.fetch(symbol, mode);
    AlphaVantageJSON alphaVantageJSON = objectMapper.readValue(alphaVantageJsonString, AlphaVantageJSON.class);
    PricesSet pricesSetToUpdate = getPricesToUpdate(alphaVantageJSON, datesToUpdate);
    if (pricesSetToUpdate == null) {
      return null;
    }
    mySQLService.insertPrices(pricesSetToUpdate);
    return String.format(updateSucceedMessage, symbol);
  }

  /**
   * Gets a PricesSet of data points to be inserted into database
   *
   * @param alphaVantageJSON Custom module storing JSON info from Alpha Vantage
   * @param datesToUpdate A list of dates on which price data is to be inserted
   * @return  PricesSet
   */
  private PricesSet getPricesToUpdate(AlphaVantageJSON alphaVantageJSON, List<String> datesToUpdate) {
    if (alphaVantageJSON.getErrorMessage() != null) return null;
    ArrayList<OneDayPrice> pricesList = new ArrayList<>();
    // Add each date to be inserted into prices list
    for (String date : datesToUpdate) {
      AlphaVantageDataPoint alphaVantageDataPoint = alphaVantageJSON.getTimeSeries().get(date);
      // If a date doesn't exist in Alpha Vantage data, then it should be a market closed date. Update database with that date.
      if (alphaVantageDataPoint == null) {
        mySQLService.insertClosedDate(date);
        continue;
      }
      OneDayPrice oneDayPrice = OneDayPrice.builder()
          .symbol(alphaVantageJSON.getMetaData().getSymbol())
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
        .symbol(alphaVantageJSON.getMetaData().getSymbol())
        .days(pricesList.size())
        .prices(pricesList)
        .build();
  }

  /**
   * Querying requests   http://{server_address}/query?symbol=ABCD&days=10
   * @param symbol Stock symbol
   * @param days   Days number that the user requests (starting from today)
   * @return
   */
  public PricesSet queryPrices(String symbol, int days) throws ParseException, IOException {
    ArrayList<String> openDatesList = getOpenDatesList(days);
    if (openDatesList.isEmpty()) {
      return PricesSet.builder().symbol(symbol).build();
    }
    // The first date in the result price data, i.e. the furthest date we want
    String startDate = openDatesList.get(openDatesList.size() - 1);
    // The last date in the result price data, i.e. yesterday date
    String endDate = openDatesList.get(0);
    ArrayList<OneDayPrice> pricesList = mySQLService.selectPrices(symbol, startDate, endDate);
    if (pricesList.size() < days) {
      ArrayList<String> datesToUpdate = getDatesToUpdate(pricesList, openDatesList);
      if (updatePrices(symbol, days, datesToUpdate) == null) {
        return null;
      }
      pricesList = mySQLService.selectPrices(symbol, startDate, endDate);
    }
    return PricesSet.builder().symbol(symbol).days(days).prices(pricesList).build();
  }

}