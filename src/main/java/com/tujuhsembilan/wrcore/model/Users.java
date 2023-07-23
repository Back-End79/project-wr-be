package com.tujuhsembilan.wrcore.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import org.springframework.context.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Primary
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`job_type_id`", referencedColumnName = "category_code_id")
  private CategoryCode jobTypeId;

  @Column(name = "nip")
  private String nip;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`placement_type`", referencedColumnName = "category_code_id")
  private CategoryCode placementType;

  @Column(name = "sso_id", columnDefinition = "text")
  private String ssoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`group`", referencedColumnName = "category_code_id")
  private CategoryCode group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`position`", referencedColumnName = "category_code_id")
  private CategoryCode position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`status_onsite`", referencedColumnName = "category_code_id")
  private CategoryCode statusOnsite;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "email")
  private String email;

  @Column(name = "last_contract_status")
  private String lastContractStatus;

  @Column(name = "last_contract_date")
  private Date lastContractDate;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "join_date")
  private Date joinDate;

  @Column(name = "end_contract_this_month")
  private boolean endContractThisMonth;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "photo_profile")
  private String photoProfile;

  @Column(name = "last_modified_on")
  private Timestamp lastModifiedOn;

  @Column(name = "last_modified_by")
  private Long lastModifiedBy;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "place_of_birth")
  private String placeOfBirth;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "identity_number")
  private String identityNumber;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "family_relationship")
  private String familyRelationship;

  @Column(name = "family_relationship_number")
  private String familyRelationshipNumber;

  @Column(name = "school")
  private String school;

  @Column(name = "education")
  private String education;

  @Column(name = "bpjs_kesehatan")
  private String bpjsKesehatan;

  @Column(name = "number_of_dependents")
  private String numberOfDependents;

  @Column(name = "bpjs_class")
  private Integer bpjsClass;

  @Column(name = "carrer_start_date")
  private Date carrerStartDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`ptkp_status`", referencedColumnName = "category_code_id")
  private CategoryCode ptkpStatus;

  @Column(name = "npwp")
  private String npwp;

  @Column(name = "no")
  private Integer no;

  @Column(name = "department")
  private String department;

}
