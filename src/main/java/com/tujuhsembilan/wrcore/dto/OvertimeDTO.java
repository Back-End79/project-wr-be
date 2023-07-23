package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Overtime")
public class OvertimeDTO {
  @Id
  @NotNull(message = "Backlog ID must not be null")
  private Long backlogId;

  @NotBlank(message = "Task name must not be blank")
  private String taskName;

  @NotNull(message = "Status task must not be null")
  private Long statusTask;

  @NotBlank(message = "Task item must not be blank")
  private String taskItem;

  @NotNull(message = "Duration must not be null")
  @Positive(message = "Duration must be a positive value")
  private BigDecimal duration;

  @JsonProperty("isOvertime")
  private boolean isOvertime;

  @NotNull(message = "Created on must not be null")
  private Timestamp createdOn;

  @NotNull(message = "Created by must not be null")
  private Long createdBy;

  private Timestamp updatedOn;

  private LocalDate workDate;

  @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Invalid time format. Use HH:mm format")
  private String startTime;

  @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Invalid time format. Use HH:mm format")
  private String endTime;
}
