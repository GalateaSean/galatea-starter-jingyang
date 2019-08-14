package org.finance_price_service.service;

import java.util.Vector;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.finance_price_service.domain.rspy.ClosedDatesRepository;
import org.finance_price_service.domain.rspy.PricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Deals with MySQL SELECT / INSERT tasks
 */
@Service
public class MySQLService {

  @Autowired
  private PricesRepository pricesRepository;

  @Autowired
  private ClosedDatesRepository closedDatesRepository;

  /**
   * SELECT
   * @param symbol Stock symbol
   * @param date   Specific date for price data
   * @return       OneDayPrice Object
   */
  public OneDayPrice select(String symbol, String date) {
    Vector<OneDayPrice> price = pricesRepository.findBySymbolAndDate(symbol, date);
    return price.isEmpty() ? null : price.firstElement();
  }

  /**
   * INSERT
   * @param prices PricesSet Object, price data to be inserted
   */
  public void insert(PricesSet prices) {
    for (OneDayPrice price : prices.getPrices()) {
      pricesRepository.save(price);
    }
  }

  /**
   * Check if market was closed on a date
   * @param date Date String, format "yyyy-MM-dd"
   * @return boolean
   */
  public boolean isCloseDay(String date) {
    return closedDatesRepository.existsByDate(date);
  }

}
