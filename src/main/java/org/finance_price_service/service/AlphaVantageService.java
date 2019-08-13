package org.finance_price_service.service;

import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Requests price data from Alpha Vantage API
 */
@Service
public class AlphaVantageService {
  @Value("${alphavantage.apikey}")
  private String apiKey;

  @Value("${alphavantage.url}")
  private String url;

  /**
   * Fetchs data from Alpha Vantage
   * @param symbol Stock symbol
   * @return       JSON String, Alpha Vantage API response
   * @throws Exception
   */
  public String fetch(String symbol, String mode) throws Exception {
    String AlphaURL = String.format(url, symbol, mode, this.apiKey);
    return IOUtils.toString(new URL(AlphaURL), Charset.forName("UTF-8"));
  }
}
