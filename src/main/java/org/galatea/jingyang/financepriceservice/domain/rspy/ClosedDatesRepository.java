package org.galatea.jingyang.financepriceservice.domain.rspy;

import org.galatea.jingyang.financepriceservice.domain.ClosedDate;
import org.springframework.data.repository.CrudRepository;

public interface ClosedDatesRepository extends CrudRepository<ClosedDate, Long> {

  boolean existsByDate(String date);

}
