package org.galatea.jingyang.financepriceservice.domain;

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
