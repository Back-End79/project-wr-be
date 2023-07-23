package com.tujuhsembilan.wrcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListDateDTO {
  private Long workingReportId;
  private Long presenceId;
  private String presenceName;
  private Long taskId;
  private Date date;
  private boolean isHoliday;
  private String descHoliday;
  private boolean isCheckIn;
  private boolean isTask;
  private boolean isOvertime;
}
