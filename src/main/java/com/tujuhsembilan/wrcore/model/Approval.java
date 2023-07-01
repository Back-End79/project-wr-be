package com.tujuhsembilan.wrcore.model;

import javax.persistence.*;

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
    private Long approvalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "period_id")
    private Period periodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users userId;

    @Column(name = "label_approval")
    private String labelApproval;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "file")
    private String file;
    
}
