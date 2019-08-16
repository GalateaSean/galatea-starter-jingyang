package org.galatea.jingyang.finance_price_service.domain.rspy;

import java.util.ArrayList;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice;
import org.galatea.jingyang.finance_price_service.domain.OneDayPrice.OneDayPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricesRepository extends JpaRepository<OneDayPrice, OneDayPriceId> {

  ArrayList<OneDayPrice> findBySymbolAndDateBetweenOrderByDateDesc(String symbol, String startDate, String endDate);

}
