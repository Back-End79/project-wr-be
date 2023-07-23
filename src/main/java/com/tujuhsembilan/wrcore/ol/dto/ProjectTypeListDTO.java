package com.tujuhsembilan.wrcore.ol.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("OptionList")
public class ProjectTypeListDTO {
    private String projectType;
    private String projectTypeName;
    @Id
    private Long projectId;
    private String projectName;

    public ProjectTypeListDTO(Project project) {
        this.projectId = project.getProjectId();
        this.projectType = project.getProjectType().getCategoryName();
        this.projectTypeName = project.getProjectType().getCodeName();
        this.projectName = project.getProjectName();
    }
}
