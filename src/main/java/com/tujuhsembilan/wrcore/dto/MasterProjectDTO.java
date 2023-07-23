package com.tujuhsembilan.wrcore.dto;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.model.Project;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Master Project")
public class MasterProjectDTO {    
    @Id
    private Long projectId;
    private Long companyId;
    private boolean isActive;    
    private String projectName;
    private String projectType;
    private String clientName;
    
    public MasterProjectDTO(Project project, Company company){
        this.projectId = project.getProjectId();
        this.companyId = project.getCompanyId().getCompanyId();
        this.isActive = project.isActive();
        this.projectName = project.getProjectName();
        this.clientName = company.getCompanyName();
        this.projectType = project.getProjectType().getCodeName();
    }
}
