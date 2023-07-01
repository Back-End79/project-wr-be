package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("UserUtilization")
public class UserUtilizationDTO {
    @Id
    private Long userUtilizationId;
    private Long userId;
    private Long companyId;
    private Long projectId;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    
}
