package com.tujuhsembilan.wrcore.dto;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("Aprproval")
public class ApprovalDTO {
    @Id
    private Long approvalId;
    private Long periodId;
    private Long userId;
    private String labelApproval;
    private String name;
    private String position;
    private String file;
}
