package org.galatea.jingyang.financepriceservice.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice;
import org.galatea.jingyang.financepriceservice.domain.PricesSet;
import org.galatea.jingyang.financepriceservice.testutils.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryLogicServiceTest {

  @Mock
  private MySQLService mySQLService;

  @Mock
  private AlphaVantageService alphaVantageService;

  private QueryLogicService queryLogicService;

  @Before
  public void setUp() {
    queryLogicService = new QueryLogicService(mySQLService, alphaVantageService, "yyyy-MM-dd", "succeed", 100);
  }

  @Test
  public void testQueryPrices_NoUpdate() throws IOException, ParseException {
    String symbol = "MSFT";
    int days = 1;
    ArrayList<OneDayPrice> testPriceList = TestDataGenerator.oneDayPriceList(1);
    Mockito.when(mySQLService.selectPrices(any(String.class), any(String.class), any(String.class))).thenReturn(testPriceList);

    PricesSet pricesSet = queryLogicService.queryPrices(symbol, days);
    assertEquals(pricesSet.getPrices(), testPriceList);
  }

  @Test
  public void testQueryPrices_WithUpdate() throws IOException, ParseException {
    String symbol = "MSFT";
    int days = 2;
    ArrayList<OneDayPrice> testPriceList = TestDataGenerator.oneDayPriceList(1);
    ArrayList<OneDayPrice> testPriceListUpdated = TestDataGenerator.oneDayPriceList(2);
    // When calling selectPrices() for the second time, the database should have been updated
    Mockito.when(mySQLService.selectPrices(any(String.class), any(String.class), any(String.class))).thenReturn(testPriceList, testPriceListUpdated);
    Mockito.when(alphaVantageService.fetch(any(String.class), any(String.class))).thenReturn(TestDataGenerator.alphaVantageJSONString(symbol, "2019-08-27"));
    Mockito.doNothing().when(mySQLService).insertPrices(any(PricesSet.class));

    PricesSet pricesSet = queryLogicService.queryPrices(symbol, days);
    assertEquals(pricesSet.getPrices().size(), 2);
    assertEquals(pricesSet.getPrices(), testPriceListUpdated);
  }

  @Test
  public void testQueryPrices_InvalidSymbol() throws IOException, ParseException {
    String symbol = "AAAA";
    int days = 1;
    Mockito.when(mySQLService.selectPrices(any(String.class), any(String.class), any(String.class))).thenReturn(new ArrayList<OneDayPrice>());
    Mockito.when(alphaVantageService.fetch(any(String.class), any(String.class))).thenReturn(TestDataGenerator.alphaVantageJSONString_Error());

    PricesSet pricesSet = queryLogicService.queryPrices(symbol, days);
    assertNull(pricesSet);
  }

  @Test
  public void testQueryPrices_InvalidDays() throws IOException, ParseException {
    String symbol = "MSFT";
    int days = -1;

    PricesSet pricesSet = queryLogicService.queryPrices(symbol, days);
    assertNull(pricesSet.getPrices());
  }

}
