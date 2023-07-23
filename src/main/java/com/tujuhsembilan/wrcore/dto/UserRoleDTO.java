package com.tujuhsembilan.wrcore.dto;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("UserRole")
public class UserRoleDTO {
    @Id
    private Long userRoleId;
    private Long userId;
    private Long roleId;
    private boolean isActive;
    private Long createdBy;
    private Timestamp createdOn;
    private Long lastModifiedBy;
    private Timestamp lastModifiedOn;
}
