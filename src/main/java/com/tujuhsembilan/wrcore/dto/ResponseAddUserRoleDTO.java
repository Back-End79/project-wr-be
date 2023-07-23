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
@JsonApiTypeForClass("Add User Role")
public class ResponseAddUserRoleDTO {    
    @Id
    private Long userRoleId;
    private Long userId;
    private List<DetailRoleDTO> detailRole;
    private boolean isActive;
    private Long createdBy;
    private Timestamp createdOn;
    private Long lastModifiedBy;
    private Timestamp lastModifiedOn;    
}
