package com.tujuhsembilan.wrcore.dto;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


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
    private Long projectType;
    private boolean isActive;
    private Long createdBy;
    private Timestamp createdOn;
    private Long lastModifiedBy;
    private Timestamp lastModifiedOn;
    private String initialProject;  
    private String projectName;
    private String projectTypeName;
}
