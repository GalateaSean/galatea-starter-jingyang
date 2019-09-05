package org.galatea.jingyang.financepriceservice.testutils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import lombok.NoArgsConstructor;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice;

/**
 * Utility class for generating objects for tests
 */
@NoArgsConstructor
public class TestDataGenerator {

  /**
   * Generate an ArrayList of OneDayPrice with only date field initialized
   *
   * @param days Number of OneDayPrice objects in the ArrayList
   * @return ArrayList of OneDayPrice
   */
  public static ArrayList<OneDayPrice> oneDayPriceList(int days) {
    ArrayList<OneDayPrice> oneDayPrices = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    for (int i = 0; i < days; ++i) {
      calendar.add(Calendar.DATE, -days);
      String date = simpleDateFormat.format(calendar.getTime());
      calendar.clear();
      oneDayPrices.add(OneDayPrice.builder().date(date).build());
    }
    return oneDayPrices;
  }

  /**
   * Generate a JSON String in Alpha Vantage format, with one data point
   *
   * @param symbol Stock symbol
   * @param date   Data point date
   * @return JSON String
   */
  public static String alphaVantageJSONString(String symbol, String date) {
    return "{\n"
        + "    \"Meta Data\": {\n"
        + "        \"1. Information\": \"Daily Prices (open, high, low, close) and Volumes\",\n"
        + "        \"2. Symbol\": \"" + symbol + "\",\n"
        + "        \"3. Last Refreshed\": \"" + date + "\",\n"
        + "        \"4. Output Size\": \"Compact\",\n"
        + "        \"5. Time Zone\": \"US/Eastern\"\n"
        + "    },\n"
        + "    \"Time Series (Daily)\": {\n"
        + "        \"2019-08-27\": {\n"
        + "            \"1. open\": \"134.9900\",\n"
        + "            \"2. high\": \"136.7200\",\n"
        + "            \"3. low\": \"134.6681\",\n"
        + "            \"4. close\": \"135.7400\",\n"
        + "            \"5. volume\": \"16639726\"\n"
        + "        }"
        + "    }"
        + "}";
  }

  /**
   * Generate a JSON String indicating an invalid call in Alpha Vantage format
   *
   * @return JSON String
   */
  public static String alphaVantageJSONString_Error() {
    return "{\n"
        + "    \"Error Message\": \"Invalid API call. Please retry or visit the documentation for TIME_SERIES_DAILY.\"\n"
        + "}";
  }

}
