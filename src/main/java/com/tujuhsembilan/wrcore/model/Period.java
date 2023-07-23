package com.tujuhsembilan.wrcore.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "period", schema = "public")
@Builder
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "period_id", unique = true, nullable = false)
    private Long periodId;

    @Column(name = "period")
    private String period;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
}
