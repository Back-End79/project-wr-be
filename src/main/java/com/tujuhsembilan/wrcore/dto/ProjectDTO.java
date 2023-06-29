package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;

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
@JsonApiTypeForClass("Project")
public class ProjectDTO {
  @Id
  private Long projectId;
  private Long companyId;
  private String picProjectName;
  private String picProjectPhone;
  private String description;
  private Date startDate;
  private Date endDate;
  private String projectType;
}
