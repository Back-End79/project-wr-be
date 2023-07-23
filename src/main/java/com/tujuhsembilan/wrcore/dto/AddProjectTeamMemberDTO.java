package com.tujuhsembilan.wrcore.dto;
import java.sql.Date;
import javax.persistence.Id;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectTeamMemberDTO {  
    @Id  
    private Long userId;
    private Long roleProjectId;
    private String roleProject;
    private Date joinDate;
    private Date endDate; 
    private boolean isActive;   
}
