package com.tujuhsembilan.wrcore.dto;
import java.util.List;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Response Update Role Privilege")
public class ResponseUpdateRolePrivilegeDTO {
    @Id
    private Long roleId;
    private String nameRole;
    private List<ListDetailPrivilegeDTO> privilegeId;
}
