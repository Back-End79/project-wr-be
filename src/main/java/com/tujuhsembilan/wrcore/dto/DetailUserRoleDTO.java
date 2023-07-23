package com.tujuhsembilan.wrcore.dto;
import java.util.List;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.UserRole;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Detail User Role")
public class DetailUserRoleDTO {   
    @Id
    private Long userRoleId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String nip;
    private List<DetailRoleDTO> listRole;

    public DetailUserRoleDTO(UserRole userRole){
        this.userRoleId = userRole.getUserRoleId();
        this.userId = userRole.getUserId().getUserId();
        this.userRoleId = userRole.getUserRoleId();
        this.firstName = userRole.getUserId().getFirstName();
        this.lastName = userRole.getUserId().getLastName();
        this.nip = userRole.getUserId().getNip();
    }
}
