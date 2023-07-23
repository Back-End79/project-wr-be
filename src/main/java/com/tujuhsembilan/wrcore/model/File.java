package com.tujuhsembilan.wrcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_id", unique = true, nullable = false)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`working_report_id`", referencedColumnName = "working_report_id")
    private WorkingReport workingReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`task_id`", referencedColumnName = "task_id")
    private Task taskId;

    @Column(name = "file_name")
    private String fileName;
}
