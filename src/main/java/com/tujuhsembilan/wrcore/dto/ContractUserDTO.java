package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("ContractUser")
public class ContractUserDTO {
    @Id
    private Long contractUserId;
    private Long userId;
    private Long categoryCode;
    private Date startDate;
    private Date endDate;
    private String file;
    private Timestamp lastModifiedOn;
    private Long lastModifieldBy;
    private Long createBy;
    private Timestamp createdOn;
}
