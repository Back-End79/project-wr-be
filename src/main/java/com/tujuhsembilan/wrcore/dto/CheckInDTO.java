package com.tujuhsembilan.wrcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {
    private Long workingReportId;
    private String latitude;
    private String longitude;
    private Time checkInTime;
    private String file;

}
