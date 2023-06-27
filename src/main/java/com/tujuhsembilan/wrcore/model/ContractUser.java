package com.tujuhsembilan.wrcore.model;

import java.sql.Date;
import java.sql.Timestamp;

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
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contract_user")
public class ContractUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`contract_user_id`")
    private Long contractUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user_id`", referencedColumnName = "user_id")
    private Users userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`contract_status`", referencedColumnName = "category_code_id")
    private CategoryCode contractStatus;

    @Column(name = "`start_date`")
    private Date startDate;

    @Column(name = "`end_date`")
    private Date endDate;

    @Column(name = "`file`")
    private String file;

    @Column(name = "`last_modified_on`")
    private Timestamp lastModifiedOn; 

    @Column(name = "`last_modified_by`")
    private Long lastModifiedBy; 
    
    @Column(name = "`created_by`")
    private Long createdBy;

    @Column(name = "`created_on`")
    private Timestamp createdOn;


}
