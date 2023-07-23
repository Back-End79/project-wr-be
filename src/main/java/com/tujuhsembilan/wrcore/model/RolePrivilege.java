package com.tujuhsembilan.wrcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

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
  @Column(name = "role_privilege_id", unique = true, nullable = false)
  private Long rolePrivilegeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", referencedColumnName = "category_code_id")
  private CategoryCode roleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "privilege_id", referencedColumnName = "category_code_id")
  private CategoryCode privilegeId;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "created_on")
  private Timestamp createdOn;
  
  @Column(name = "updated_on")
  private Timestamp updateOn;

  @Column(name = "updated_by")
  private Long updateBy;
  
}

