package com.tujuhsembilan.wrcore.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonApiTypeForClass("List Task Item")
public class listTaskItemDTO {
    @Id
     private Long backlogId;
    private Long workingReportId;
    private String taskItem;
    private String taskDetail;
    private BigDecimal duration;
    private boolean isOvertime;
    private Timestamp createdOn;
    private Long createdBy;
    private Timestamp updateOn;
    private Long updatedBy;
    private Date workDate;
}
