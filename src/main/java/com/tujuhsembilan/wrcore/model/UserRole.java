package com.tujuhsembilan.wrcore.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_role", schema = "public")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_role_id", unique = true, nullable = false)
  private Long userRoleId;

  @Column(name = "user_id")
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", referencedColumnName = "category_code_id")
  private CategoryCode roleId;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "last_modified_on")
  private Timestamp lastModifiedOn;

  @Column(name = "last_modified_by")
  private Long lastModifiedBy;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "created_by")
  private Long createdBy;
}
