package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Task")
public class TaskDTO {
  @Id
  private Long taskId;
  private Long backlogId;
  private Long workingReportId;
  private String taskItem;
  private BigDecimal duration;
  private boolean isOvertime;
  private Timestamp createdOn;
  private Long createBy;
  private Timestamp updateOn;
  private Long updateBy;
}