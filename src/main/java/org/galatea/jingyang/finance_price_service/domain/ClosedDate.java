package org.galatea.jingyang.finance_price_service.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "closed_dates")
@Getter
public class ClosedDate {

  @Id
  private String date;

}
