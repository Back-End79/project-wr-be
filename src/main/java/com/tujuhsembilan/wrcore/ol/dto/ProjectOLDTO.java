package com.tujuhsembilan.wrcore.ol.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("OptionList")
public class ProjectOLDTO {

  private Long id;
  private String name;

  public ProjectOLDTO(Project project) {
    this.id = project.getProjectId();
    this.name = project.getProjectName();
  }

}
