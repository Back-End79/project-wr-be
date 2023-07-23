package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.validation.constraints.*;

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

  @NotNull(message = "Project ID must not be empty.")
  private Long projectId;

  @NotNull(message = "Status Backlog must not be empty.")
  private Long statusBacklog;

  @NotNull(message = "User ID must not be empty.")
  private Long userId;

  private String projectName;

  private String status;

  private String assignedTo;

  @NotBlank(message = "Task name must not be empty.")
  private String taskName;

  @NotBlank(message = "Task description must not be empty.")
  private String taskDescription;

  @NotNull(message = "Estimation time must not be null.")
  @DecimalMin(value = "0", inclusive = false, message = "Estimation time must be greater than 0.")
  private BigDecimal estimationTime;

  @NotNull(message = "Actual time must not be null.")
  @DecimalMin(value = "0", inclusive = false, message = "Actual time must be greater than 0.")
  private BigDecimal actualTime;

  @NotNull(message = "Estimation date must not be null.")
  @FutureOrPresent(message = "Estimation date must be now or in the future.")
  private Date estimationDate;

  @NotNull(message = "Actual date must not be null.")
  @FutureOrPresent(message = "Actual date must be now or in the future.")
  private Date actualDate;

  @NotNull(message = "Created by ID must not be null.")
  private Long createdBy;

  @NotNull(message = "Updated by ID must not be null.")
  private Long updatedBy;

  private Timestamp createdOn;

  private Timestamp updatedOn;

  @NotBlank(message = "Priority must not be empty.")
  private String priority;

  @NotBlank(message = "Task code must not be empty.")
  private String taskCode;
}
