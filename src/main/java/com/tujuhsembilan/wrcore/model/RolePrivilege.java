package com.tujuhsembilan.wrcore.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@Primary
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_privilege")
@Builder
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

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "updated_on")
  private Timestamp updatedOn;
}
