package org.galatea.jingyang.finance_price_service.service;

import java.util.ArrayList;
import java.util.Optional;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice.OneDayPriceId;
import org.galatea.jingyang.finance_price_service.domain.PricesSet;
import org.galatea.jingyang.finance_price_service.domain.rspy.ClosedDatesRepository;
import org.galatea.jingyang.finance_price_service.domain.rspy.PricesRepository;
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
    OneDayPriceId id = OneDayPriceId.builder().date(date).symbol(symbol).build();
    Optional<OneDayPrice> price = pricesRepository.findById(id);
    return price.orElse(null);
  }

  /**
   * SELECT Gets the prices of a specific stock between starting date and ending date
   *
   * @param symbol    Stock symbol
   * @param startDate Starting date
   * @param endDate   Ending date
   * @return
   */
  public ArrayList<OneDayPrice> selectPrices(String symbol, String startDate, String endDate) {
    return pricesRepository.findBySymbolAndDateBetweenOrderByDateDesc(symbol, startDate, endDate);
  }

  /**
   * INSERT A list of price records
   *
   * @param prices PricesSet Object, price data to be inserted
   */
  public void insertPrices(PricesSet prices) {
    for (OneDayPrice price : prices.getPrices()) {
      pricesRepository.save(price);
    }
  }

  /**
   * INSERT A single price record
   *
   * @param oneDayPrice
   */
  public void insertSinglePrice(OneDayPrice oneDayPrice) {
    pricesRepository.save(oneDayPrice);
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
