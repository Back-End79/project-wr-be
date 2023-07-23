package com.tujuhsembilan.wrcore.dto;

import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Company")
public class CompanyDTO {
  @Id
  private Long companyId;
  private String companyName;
  private String companyEmail;
  private String address;
  private String npwp;
  private String companyProfile;
  @JsonProperty("isActive")
  private boolean isActive;
  private Long createdBy;
  private Timestamp createdOn;
  private Timestamp lastModifiedOn;
  private Long lastModifiedBy;
}