package com.tujuhsembilan.wrcore.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodDTO {
	private Long periodId;
	private String period;
	private Date startDate;
	private Date endDate;
}