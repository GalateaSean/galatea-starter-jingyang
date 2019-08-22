package org.galatea.jingyang.financepriceservice.entrypoint;
  import java.io.IOException;
  import java.text.ParseException;
  import org.galatea.jingyang.financepriceservice.domain.PricesSet;
  import org.galatea.jingyang.financepriceservice.service.QueryLogicService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;

/**
 * Calls QueryLogicService for dealing with requests
 */
@RestController
public class PricesController {

  @Autowired
  private QueryLogicService logic;

  /**
   * "Query" request, gets stock prices data of a number of dates from today.
   *  See README.md for example
   * @param symbol Stock symbol
   * @param days   Days number that the user wants the data counting back from current date
   * @return       PricesSet Object
   * @throws IOException, ParseException
   */
  @GetMapping("/query")
  public PricesSet queryRequest(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "days", defaultValue = "0") int days)
      throws IOException, ParseException {
    return logic.queryPrices(symbol, days);
  }

}