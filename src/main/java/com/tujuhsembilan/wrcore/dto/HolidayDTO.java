package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Holiday")
public class HolidayDTO {
    @Id
    private Long holidayId;
    private Date date;
    private String notes;
    
}
