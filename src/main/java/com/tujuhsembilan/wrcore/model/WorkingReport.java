package com.tujuhsembilan.wrcore.model;

import java.math.BigDecimal;
import java.sql.*;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "working_report", schema = "public")
@Entity
public class WorkingReport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "working_report_id", unique = true, nullable = false)
  private Long workingReportId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`period_id`", referencedColumnName = "period_id")
  private Period periodId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`presence_id`", referencedColumnName = "category_code_id")
  private CategoryCode presenceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`user_id`", referencedColumnName = "user_id")
  private Users userId;

  @Column(name = "date")
  private Date date;

  @Column(name = "check_in")
  private Time checkIn;

  @Column(name = "check_out")
  private Time checkOut;

  @Column(name = "work_location")
  private String workLocation;

  @Column(name = "total_hours")
  private BigDecimal totalHours;

  @Column(name = "created_time")
  private Timestamp createdTime;

  @Column(name = "last_modified_time")
  private Timestamp lastModifiedTime;

  @Column(name = "is_holiday")
  private boolean isHoliday;

  @Column(name = "location_checkin", columnDefinition = "text")
  private String locationCheckin;

  @Column(name = "location_checkout", columnDefinition = "text")
  private String locationCheckout;

}