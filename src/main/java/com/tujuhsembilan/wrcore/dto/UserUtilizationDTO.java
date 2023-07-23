package com.tujuhsembilan.wrcore.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor

public class UserUtilizationDTO {

    
    @Id
    private Long userUtilizationId;
    private UserDTO userId;
    private CompanyDTO companyId;
    private ProjectDTO projectId;    
    private Date startDate;
    private Date endDate;
    private Long createdBy;
    private Timestamp createdOn;
    private boolean isActive;
}
