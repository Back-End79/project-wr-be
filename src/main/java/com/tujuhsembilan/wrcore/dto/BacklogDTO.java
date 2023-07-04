package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Backlog")
public class BacklogDTO {
  @Id
  private Long backlogId;
  private Long projectId;
  private Long statusBacklog;
  private Long userId;
  private String projectName;
  private String status;
  private String assignedTo;
  private String taskName;
  private String taskDescription;
  private BigDecimal estimationTime;
  private BigDecimal actualTime;
  private Date estimationDate;
  private Date actualDate;
  private Long createdBy;
  private Long updatedBy;
  private Timestamp createdOn;
  private Timestamp updatedOn;
  private String priority;
  private String taskCode;
}
