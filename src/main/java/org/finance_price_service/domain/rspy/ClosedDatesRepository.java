package org.finance_price_service.domain.rspy;

import org.finance_price_service.domain.ClosedDate;
import org.springframework.data.repository.CrudRepository;

public interface ClosedDatesRepository extends CrudRepository<ClosedDate, Long> {
  boolean existsByDate(String date);
}
