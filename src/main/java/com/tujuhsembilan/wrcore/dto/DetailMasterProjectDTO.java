package com.tujuhsembilan.wrcore.dto;
import java.sql.Date;
import java.util.List;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Detail Master Project")
public class DetailMasterProjectDTO {
    @Id
    private Long projectId;    
    private String companyName;
    private String picProjectName;
    private String picProjectPhone;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String initialProject;  
    private String projectName;
    private String projectTypeName;
    private List<DetailMasterProjectTeamMemberDTO> teamMember;       
}
