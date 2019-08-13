package org.finance_price_service.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "closed_dates")
public class ClosedDate {
  @Id
  private String date;

  public String getDate() { return this.date; }
}
