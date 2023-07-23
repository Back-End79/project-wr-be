package com.tujuhsembilan.wrcore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTaskDTO {
    private Long workingReportId;
    private Long BacklogId;
    private List<listTaskItemDTO> listTask;
}
