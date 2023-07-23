package com.tujuhsembilan.wrcore.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonApiTypeForClass("List Holiday")
public class listHolidayDTO {
    private String date;
    private String weekdays;
    private String name;

}
