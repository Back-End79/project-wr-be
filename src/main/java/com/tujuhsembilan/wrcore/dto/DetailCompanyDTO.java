package com.tujuhsembilan.wrcore.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Company")
public class DetailCompanyDTO {
    @Id
    private Long companyId;
    private String companyName;
    private String companyEmail;
    private String address;
    private String npwp;
    private String companyProfile;
    private List<DetailCompanyListProjectDTO> projects;

    public DetailCompanyDTO(Company company){
        this.companyId = company.getCompanyId();
        this.companyName = company.getCompanyName();
        this.companyEmail = company.getCompanyEmail();
        this.address = company.getAddress();
        this.npwp = company.getNpwp();
        this.companyProfile = company.getCompanyProfile();
    }
}
