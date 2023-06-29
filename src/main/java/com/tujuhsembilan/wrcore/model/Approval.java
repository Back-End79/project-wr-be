package com.tujuhsembilan.wrcore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "approval", schema = "public")
@Builder
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "approval_id", unique = true, nullable = false)
    private Long ApprovalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "period_id")
    private Long periodId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Long userId;

    @Column(name = "label_approval")
    private String labelApproval;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "file")
    private String file;
    
}
