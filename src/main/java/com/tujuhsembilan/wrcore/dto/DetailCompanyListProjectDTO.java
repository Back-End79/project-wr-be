package com.tujuhsembilan.wrcore.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCompanyListProjectDTO {
    private String projectName;
    private String projectType;

    public DetailCompanyListProjectDTO(Project p){
        this.projectName = p.getProjectName();
        this.projectType = p.getProjectType().getCodeName();
    }
}
