package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTaskDTO {
  private Long taskId;
  private Long backlogId;
  private String taskCode;
  private String taskName;
  private Long statusTaskId;
  private String statusTaskName;
  private String taskDescription;
  private String taskItem;
  private BigDecimal duration;
  private Timestamp createdOn;
  private Long createBy;
  private Timestamp updateOn;
  private Long updateBy;
}
