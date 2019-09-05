package org.galatea.jingyang.financepriceservice.domain.rspy;

import org.galatea.jingyang.financepriceservice.domain.ClosedDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosedDatesRepository extends CrudRepository<ClosedDate, Long> {

  boolean existsByDate(String date);

}
