package org.galatea.jingyang.financepriceservice.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageDataPoint;
import org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageJSON;
import org.galatea.jingyang.financepriceservice.domain.modelresponse.AlphaVantageMetaData;
import org.galatea.jingyang.financepriceservice.testutils.TestDataGenerator;
import org.junit.Test;

public class ModelResponseTest {

  @Test
  public void testModel() throws IOException {
    String symbol = "MSFT";
    String date = "2019-08-27";
    String alphaVantageJSONString = TestDataGenerator.alphaVantageJSONString(symbol, date);
    ObjectMapper objectMapper = new ObjectMapper();
    AlphaVantageJSON alphaVantageJSON = objectMapper.readValue(alphaVantageJSONString, AlphaVantageJSON.class);
    AlphaVantageMetaData alphaVantageMetaData = alphaVantageJSON.getMetaData();
    Map<String, AlphaVantageDataPoint> alphaVantageJSONTimeSeries = alphaVantageJSON.getTimeSeries();

    assertNotNull("Meta Data should not be null", alphaVantageMetaData);
    assertNotNull("Time Series should not be null", alphaVantageJSONTimeSeries);
    assertNotNull("Symbol field in Meta Data should not be null", alphaVantageMetaData.getSymbol());
    assertEquals("There should be 1 data point in Time Series", 1, alphaVantageJSONTimeSeries.size());
  }

}
