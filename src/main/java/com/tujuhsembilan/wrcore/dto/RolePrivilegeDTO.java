package com.tujuhsembilan.wrcore.dto;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Role Privilege")
public class RolePrivilegeDTO {
  @Id
  private Long rolePrivilegeId;
  private Long roleId;
  private Long privilegeId;
  private String privilegeCategoryName; 
  private String roleCategoryName; 
    
}
