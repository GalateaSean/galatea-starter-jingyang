package org.finance_price_service.entrypoint;
  import org.finance_price_service.domain.PricesSet;
  import org.finance_price_service.service.QueryLogicService;
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
  @RequestMapping("/query")
  public PricesSet response(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "days", defaultValue = "0") int days)
      throws Exception {
    return logic.query(symbol, days);
  }

  @RequestMapping("/update")
  public String update(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "mode", defaultValue = "compact") String mode) throws Exception {
    return logic.update(symbol, mode);
  }

}