package com.tujuhsembilan.wrcore.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_privilege")
public class RolePrivilege {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_privilege_id")
  private Long rolePrivilegeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", referencedColumnName = "category_code_id")
  private CategoryCode roleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "privilege_id", referencedColumnName = "category_code_id")
  private CategoryCode privilegeId;
}
