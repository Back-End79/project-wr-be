package com.tujuhsembilan.wrcore.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonApiTypeForClass("Add Role Privilege")
public class AddRolePrivilegeDTO {
  @Id
  private Long rolePrivilegeId;
  private Long roleId;
  private String roleName;
  private List<Long> listPrivilege;
  private Long createdBy;
  private Timestamp createdOn;
  private Timestamp updatedOn;
  private Long updatedBy;
}
