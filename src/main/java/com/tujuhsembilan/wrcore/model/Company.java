package com.tujuhsembilan.wrcore.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_id")
  private Long companyId;

  @Column(name = "company_name")
  private String companyName;

  @Column(name = "company_email")
  private String companyEmail;

  @Column(name = "address")
  private String address;

  @Column(name = "npwp")
  private String npwp;

  @Column(name = "company_profile", columnDefinition = "text")
  private String companyProfile;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "last_modified_on")
  private Timestamp lastModifiedOn;

  @Column(name = "last_modified_by")
  private Long lastModifiedBy;
}
