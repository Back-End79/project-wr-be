package com.tujuhsembilan.wrcore.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "task_id")
  private Long taskId;

  @OneToOne
  @JoinColumn(name = "backlog_id", referencedColumnName = "backlog_id")
  private Backlog backlogId;

  @OneToOne
  @JoinColumn(name = "working_report_id", referencedColumnName = "working_report_id")
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
}