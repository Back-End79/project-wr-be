package com.tujuhsembilan.wrcore.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "backlog")
public class Backlog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "backlog_id")
  private Long backlogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`project_id`", referencedColumnName = "project_id")
  private Project projectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`status_backlog`", referencedColumnName = "category_code_id")
  private CategoryCode statusBacklog;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`user_id`", referencedColumnName = "user_id")
  private Users userId;

  @Column(name = "task_name", length = 100)
  private String taskName;

  @Column(name = "task_description", length = 255)
  private String taskDescription;

  @Column(name = "estimation_time")
  private BigDecimal estimationTime;

  @Column(name = "actual_time")
  private BigDecimal actualTime;

  @Column(name = "estimation_date")
  private Date estimationDate;

  @Column(name = "actual_date")
  private Date actualDate;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "updated_on")
  private Timestamp updatedOn;

  @Column(name = "priority", length = 10)
  private String priority;

}
