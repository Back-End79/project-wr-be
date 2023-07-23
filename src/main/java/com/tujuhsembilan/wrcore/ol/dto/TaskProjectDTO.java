package com.tujuhsembilan.wrcore.ol.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Backlog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("OptionList")
public class TaskProjectDTO {
    @Id
    private Long backlogId;
    private String taskName;
    private Long backlogStatusId;
    private String backlogStatus;
    private BigDecimal actualEffort;
    private String taskDetail;


    public TaskProjectDTO(Backlog backlog){
        this.backlogId = backlog.getBacklogId();
        this.taskName = backlog.getTaskCode() + " - " + backlog.getTaskName();
        this.backlogStatusId = backlog.getStatusBacklog().getCategoryCodeId();
        this.backlogStatus = backlog.getStatusBacklog().getCodeName();
        this.actualEffort = backlog.getActualTime();
        this.taskDetail = backlog.getTaskDescription();
    }
}
