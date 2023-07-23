package com.tujuhsembilan.wrcore.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Working Report")
public class WorkingReportDTO {
  @JsonApiId
  private String id;

  @JsonApiType
  private String type = "Working Report";

  private Long period;
  private long userId;
  private String name;
  private String roleName;
  private ListDateDTO listDate;
}
