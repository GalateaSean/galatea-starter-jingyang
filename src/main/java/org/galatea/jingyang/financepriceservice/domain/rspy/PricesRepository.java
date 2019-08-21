package org.galatea.jingyang.financepriceservice.domain.rspy;

import java.util.ArrayList;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice;
import org.galatea.jingyang.financepriceservice.domain.OneDayPrice.OneDayPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricesRepository extends JpaRepository<OneDayPrice, OneDayPriceId> {

  ArrayList<OneDayPrice> findBySymbolAndDateBetweenOrderByDateDesc(String symbol, String startDate, String endDate);

}
