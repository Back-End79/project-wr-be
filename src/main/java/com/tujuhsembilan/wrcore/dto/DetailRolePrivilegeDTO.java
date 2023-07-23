package com.tujuhsembilan.wrcore.dto;
import java.util.List;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.CategoryCode;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Detail Role Privilege")
public class DetailRolePrivilegeDTO {
    @Id
    private Long roleId;
    private String roleName;
    private List<ListDetailPrivilegeDTO> listPrivilege;

    public DetailRolePrivilegeDTO(CategoryCode rolePrivilege){
        this.roleId = rolePrivilege.getCategoryCodeId();
        this.roleName = rolePrivilege.getCodeName();
    }
}
