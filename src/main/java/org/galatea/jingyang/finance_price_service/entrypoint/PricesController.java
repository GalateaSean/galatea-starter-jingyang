package org.galatea.jingyang.finance_price_service.entrypoint;
  import org.galatea.jingyang.finance_price_service.domain.PricesSet;
  import org.galatea.jingyang.finance_price_service.service.QueryLogicService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.RequestMapping;
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
   * @throws Exception
   */
  @RequestMapping("/query")
  public PricesSet response(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "days", defaultValue = "0") int days)
      throws Exception {
    return logic.query(symbol, days);
  }

  /**
   * "Update" request, updates stock price data in DB.
   *  Not necessary for common usage, since "query" request updates database automatically if data is missing.
   *  See README.md for example.
   * @param symbol Stock symbol
   * @param mode   "compact": updates 100 data points from today on; "full": updates data points of recent 20 years
   * @return       Update succeed message, or null
   * @throws Exception
   */
  @RequestMapping("/update")
  public String update(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "mode", defaultValue = "compact") String mode) throws Exception {
    return logic.update(symbol, mode);
  }

}