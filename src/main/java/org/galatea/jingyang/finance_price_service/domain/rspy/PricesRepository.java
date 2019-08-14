package org.galatea.jingyang.finance_price_service.domain.rspy;

import java.util.ArrayList;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricesRepository extends CrudRepository<OneDayPrice, Long> {
  ArrayList<OneDayPrice> findBySymbolAndDate(String symbol, String date);
}
