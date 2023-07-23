package com.tujuhsembilan.wrcore.dto;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Update Role Privilege")
public class UpdateRolePrivilegeDTO {
    @Id
    private Long userRoleId;
    private Long roleId;
    private List<Long> privilegeId;
    private boolean isActive;
    private Long createdBy;
    private Timestamp createdOn;
    private Long updatedBy;
    private Timestamp updatedOn;   
}
