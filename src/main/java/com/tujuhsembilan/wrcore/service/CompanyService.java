package com.tujuhsembilan.wrcore.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tujuhsembilan.wrcore.dto.DetailCompanyDTO;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.CompanyDTO;
import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final ProjectRepository projectRepository;

  public Page<Company> getAllCompany(Pageable pageable, String search) {
    return companyRepository.findByCompanyName(search, pageable);
  }

  @Transactional
  public void deleteCompany(Long id) {
    Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company Data Not Found!"));
    List<Company> listIsActiveCompany = companyRepository.listActiveProject(company.getCompanyId());
    if (listIsActiveCompany.isEmpty()) {
      company.setActive(false);
      companyRepository.save(company);
    } else {
      throw new EntityNotFoundException("Company Data Not Found!");
    }
  }

  public List<CompanyDTO> convertCompanyToDto(Page<Company> company) {
    List<CompanyDTO> companyDTO = company.getContent().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
    return companyDTO;
  }

  public Company getCompanyById(Long companyId) {
    Company company = companyRepository.findByCompanyId(companyId)
        .orElseThrow(() -> new EntityNotFoundException("Company Data Not Found!"));
    return company;
  }

  public DetailCompanyDTO getDetailCompany(Long companyId) {
    Company company = companyRepository.findByCompanyId(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company Data Not Found!"));
    DetailCompanyDTO res = companyRepository.getDetailCompany(company.getCompanyId());
    res.setProjects(projectRepository.getDetailCompanyListProject(company.getCompanyId()));
    return res;
  }

  public Company updateCompany(Long companyId, CompanyDTO companyDTO) {
    Company existingCompany = getCompanyById(companyId);

    Company updatedCompany = updateCompanyMapper(existingCompany, companyDTO);
    Company savedCompany = companyRepository.save(updatedCompany);

    return savedCompany;
  }

  public Company addCompany(CompanyDTO companyDTO) {
    Company company = Company.builder()
        .companyName(companyDTO.getCompanyName())
        .companyEmail(companyDTO.getCompanyEmail())
        .address(companyDTO.getAddress())
        .npwp(companyDTO.getNpwp())
        .companyProfile(companyDTO.getCompanyProfile())
        .isActive(true)
        .createdBy(companyDTO.getCreatedBy())
        .createdOn(new Timestamp(System.currentTimeMillis()))
        .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
        .lastModifiedBy(companyDTO.getLastModifiedBy())
        .build();
    companyRepository.save(company);
    return company;
  }

  public Company updateCompanyMapper(Company existingCompany, CompanyDTO companyDTO) {
    return Company.builder()
        .companyId(existingCompany.getCompanyId())
        .companyName(companyDTO.getCompanyName())
        .companyEmail(companyDTO.getCompanyEmail())
        .address(companyDTO.getAddress())
        .npwp(companyDTO.getNpwp())
        .companyProfile(companyDTO.getCompanyProfile())
        .isActive(existingCompany.isActive())
        .createdBy(existingCompany.getCreatedBy())
        .createdOn(existingCompany.getCreatedOn())
        .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
        .lastModifiedBy(companyDTO.getLastModifiedBy())
        .build();
  }

   private CompanyDTO convertToDto(Company company) {
     return CompanyDTO.builder()
         .companyId(company.getCompanyId())
         .companyName(company.getCompanyName())
         .companyEmail(company.getCompanyEmail())
         .address(company.getAddress())
         .npwp(company.getNpwp())
         .companyProfile(company.getCompanyProfile())
         .isActive(true)
         .createdBy(company.getCreatedBy())
         .createdOn(new Timestamp(System.currentTimeMillis()))
         .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
         .lastModifiedBy(company.getLastModifiedBy())
         .build();
  }
}
