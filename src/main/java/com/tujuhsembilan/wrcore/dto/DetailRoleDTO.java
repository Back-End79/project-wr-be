package com.tujuhsembilan.wrcore.dto;
import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Detail Role")
public class DetailRoleDTO {
    @Id
    private Long userRoleId;
    private Long roleId;
    private boolean isActive;    
    private String role;
    
}
