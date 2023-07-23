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
@JsonApiTypeForClass("Update User Role")
public class UpdateUserRoleDTO {
    @Id
    private Long userRoleId;
    private Long userId;
    private List<Long> roleId;
    private boolean isActive;
    private Long createdBy;
    private Timestamp createdOn;
    private Long lastModifiedBy;
    private Timestamp lastModifiedOn;   
}
