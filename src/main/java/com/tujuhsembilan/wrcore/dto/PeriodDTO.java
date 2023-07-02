package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("Period")
public class PeriodDTO {
    @Id
    private Long periodId;
    private String period;
    private Date startDate;
    private Date endDate;
    
}
