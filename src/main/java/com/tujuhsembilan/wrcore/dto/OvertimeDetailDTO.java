package com.tujuhsembilan.wrcore.dto;

import java.util.List;

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
@JsonApiTypeForClass("Overtime")
public class OvertimeDetailDTO {
  @Id
  private Long workingReportId;
  private Long projectId;
  private String projectName;
  private List<ListTaskDTO> listTask;
}
