package com.tujuhsembilan.wrcore.model;

import java.sql.Date;

import javax.persistence.*;

import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_utilization")
public class UserUtilization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_utilization_id")
    private Long userUtilizationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private long companyId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Long projectId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_active")
    private boolean isActive;
    
}
