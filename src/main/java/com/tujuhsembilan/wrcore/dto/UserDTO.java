package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("User")
public class UserDTO {
  @Id
  private Long userId;
  private Long jobTypeId;
  private String nip;
  private Long placementType;
  private String ssoId;
  private Long group;
  private Long position;
  private Long statusOnsite;
  private String userName;
  private String email;
  private String lastContractStatus;
  private Date lastContractDate;
  private String firstName;
  private String lastName;
  private Date joinDate;
  private boolean endContractThisMonth;
  @JsonProperty("isActive")
  private boolean isActive;
  private String photoProfile;
  private Timestamp lastModifiedOn;
  private Long lastModifiedBy;
  private Long createdBy;
  private Timestamp createdOn;
  private String placeOfBirth;
  private Date dateOfBirth;
  private String identityNumber;
  private String postalCode;
  private String familyRelationship;
  private String familyRelationshipNumber;
  private String school;
  private String education;
  private String bpjsKesehatan;
  private String numberOfDependents;
  private Integer bpjsClass;
  private Date carrerStartDate;
  private Long ptkpStatus;
  private String npwp;
  private Integer no;
  private String department;
}
