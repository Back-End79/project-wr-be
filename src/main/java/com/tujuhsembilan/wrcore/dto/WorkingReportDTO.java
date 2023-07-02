package com.tujuhsembilan.wrcore.dto;

import java.sql.*;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("WorkingReport")
public class WorkingReportDTO {
    @Id
    private Long workingReportId;
    private Long periodId;
    private Long presenceId;
    private Long userId;
    private Date date;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private String workLocation;
    private int totalHours;
    private Timestamp createTime;
    private Timestamp lastModifiedTime;
    private boolean isHoliday;
    private String locationCheckin;
    private String locationCheckout;
}
