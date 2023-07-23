package com.tujuhsembilan.wrcore.dto;

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
@JsonApiTypeForClass("Employee Attendance")
public class EmployeeAttendanceResponseDTO {
    private Long id;

    @JsonApiType
    public Long getWorkingReportId()
    {
        return this.id;
    }
}
