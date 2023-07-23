package com.tujuhsembilan.wrcore.dto;
import java.sql.Date;
import java.util.List;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonApiTypeForClass("Project")
public class AddProjectDTO {
    @Id
    private Long projectId;    
    private Long companyId;
    private Long createdBy;
    private Long lastModifiedBy;
    private String companyName;
    private String picProjectName;
    private String picProjectPhone;
    private String description;
    private Date startDate;
    private Date endDate;
    private Long projectType;
    private boolean isActive;
    private String initialProject;  
    private String projectName;
    private String projectTypeName;
    private List<AddProjectTeamMemberDTO> teamMember;    
}
