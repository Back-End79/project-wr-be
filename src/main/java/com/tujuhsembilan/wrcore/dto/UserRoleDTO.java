package com.tujuhsembilan.wrcore.dto;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("UserRole")
public class UserRoleDTO {
    private Long userId;
    private Long roleId;
}
