package com.tujuhsembilan.wrcore.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long taskId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`backlog_id`", referencedColumnName = "backlog_id")
  private Backlog backlogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`working_report_id`", referencedColumnName = "working_report_id")
  private WorkingReport workingReportId;

  @Column(name = "task_item")
  private String taskItem;

  @Column(name = "duration")
  private BigDecimal duration;

  @Column(name = "is_overtime")
  private boolean isOvertime;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_on")
  private Timestamp updatedOn;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "work_date")
  private LocalDate workDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`absence_id`", referencedColumnName = "category_code_id")
  private CategoryCode absenceId;
}