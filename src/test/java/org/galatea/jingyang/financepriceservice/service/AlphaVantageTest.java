package org.galatea.jingyang.financepriceservice.service;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class AlphaVantageTest {

  private final static String apiKey = "RFISYPX8K6DYSF40";
  private final static String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&outputsize=%s&apikey=%s";
  private AlphaVantageService alphaVantageService;

  @Before
  public void setUp() {
    alphaVantageService = new AlphaVantageService(apiKey, url);
  }

  @Test
  public void testCompactFetch() throws IOException {
    String symbol = "MSFT";
    String mode = "compact";

    String fetchedRes = alphaVantageService.fetch(symbol, mode);
    assertNotNull(fetchedRes);
    assertTrue("Replied Alpha Vantage JSON must contain \"Meta Data\"", fetchedRes.contains("Meta Data"));
  }

  @Test
  public void testFullFetch() throws IOException {
    String symbol = "MSFT";
    String mode = "full";

    String fetchedRes = alphaVantageService.fetch(symbol, mode);
    assertNotNull(fetchedRes);
    assertTrue("Replied Alpha Vantage JSON must contain \"Meta Data\"", fetchedRes.contains("Meta Data"));
  }

  @Test
  public void testFetchFailure() throws IOException {
    String symbol = "AAAA";
    String mode = "compact";

    String fetchedRes = alphaVantageService.fetch(symbol, mode);
    assertNotNull(fetchedRes);
    assertTrue("On invalid call, replied Alpha Vantage JSON must contain \"Error Message\"", fetchedRes.contains("Error Message"));
  }

}
