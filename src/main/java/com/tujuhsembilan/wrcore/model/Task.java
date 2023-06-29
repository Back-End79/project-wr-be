package com.tujuhsembilan.wrcore.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task{ 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long taskId;

    @OneToOne
    @JoinColumn(name = "backlog_id", referencedColumnName = "backlog_id")
    private Long backlogId;

    @OneToOne
    @JoinColumn(name = "working_report_id", referencedColumnName = "working_report_id")
    private Long workingReportId;

    @OneToOne
    @JoinColumn(name = "category_code_id", referencedColumnName = "category_code_id")
    private Long categoryCodeId;

    @Column(name = "task_item")
    private String taskItem;

    @Column(name = "duration")
    private int duration;

    @Column(name = "is_overtime")
    private boolean isOvertime;

    @Column(name = "file_path")
    private String filePath;
}