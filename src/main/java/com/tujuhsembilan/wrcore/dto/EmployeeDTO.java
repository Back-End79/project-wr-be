package com.tujuhsembilan.wrcore.dto;

import java.sql.Date;

import javax.persistence.Id;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    @Id
    private Long userId;
    private String nip;
    private String fullName;
    private String photoProfile;
    private String lastContractStatus;
    private String assingment;
    private Date lastContractDate;
}