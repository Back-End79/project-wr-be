package com.tujuhsembilan.wrcore.dto;

import javax.persistence.Id;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

    @Id
    private Long taskId;
    private Long backlogId;
    private Long workingReportId;
    private Long categoryCodeId;
    private String taskItem;
    private int duration;
    private boolean isOvertime;
    private String filePath;

}