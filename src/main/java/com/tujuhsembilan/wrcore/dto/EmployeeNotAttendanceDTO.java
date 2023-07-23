package com.tujuhsembilan.wrcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeNotAttendanceDTO {
    private Long presenceId;
    private Long periodId;
    private Date date;
    private String file;
    private Long userId;
}
