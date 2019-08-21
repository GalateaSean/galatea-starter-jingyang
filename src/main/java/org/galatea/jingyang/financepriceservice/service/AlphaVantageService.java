package org.galatea.jingyang.financepriceservice.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Requests price data from Alpha Vantage API
 */
@NoArgsConstructor
@Service
public class AlphaVantageService {

  @Value("${alphavantage.apikey}")
  private String apiKey;

  @Value("${alphavantage.url}")
  private String url;

  /**
   * Fetchs data from Alpha Vantage
   *
   * @param symbol Stock symbol
   * @return       JSON String, Alpha Vantage API response
   * @throws IOException
   */
  public String fetch(String symbol, String mode) throws IOException {
    String alphaURL = String.format(url, symbol, mode, this.apiKey);
    return IOUtils.toString(new URL(alphaURL), StandardCharsets.UTF_8);
  }

}
