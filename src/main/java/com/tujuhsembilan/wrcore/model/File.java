package com.tujuhsembilan.wrcore.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_id")
    private Long fileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "working_report_id", referencedColumnName = "working_report_id")
    private Long workingReportId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Long taskId;

    @Column(name = "file_name")
    private String fileName;
}
