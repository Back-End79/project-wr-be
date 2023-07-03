package com.tujuhsembilan.wrcore.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.dto.CompanyDTO;
import com.tujuhsembilan.wrcore.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final ModelMapper modelMapper;

  public Page<Company> getAllCompany(Pageable pageable) {
    return companyRepository.findAll(pageable);
  }

  public Company addCompany(CompanyDTO companyDto) {
    Company company = Company.builder()
        .companyName(companyDto.getCompanyName())
        .companyEmail(companyDto.getCompanyEmail())
        .picName(companyDto.getPicName())
        .picEmail(companyDto.getPicEmail())
        .picPhone(companyDto.getPicPhone())
        .address(companyDto.getAddress())
        .npwp(companyDto.getNpwp())
        .email(companyDto.getEmail())
        .companyProfile(companyDto.getCompanyProfile())
        .build();
    companyRepository.save(company);
    return company;
  }

  public Company getCompanyById(Long companyId) throws NotFoundException {
    Company company = companyRepository.findByCompanyId(companyId)
        .orElseThrow(() -> new NotFoundException());
    return company;
  }

  public Company updateCompany(Long companyId, CompanyDTO companyDTO) throws NotFoundException {
    Company existingCompany = getCompanyById(companyId);

    Company updatedCompany = convertToEntity(companyDTO);
    updatedCompany.setCompanyId(existingCompany.getCompanyId());
    Company savedCompany = companyRepository.save(updatedCompany);

    return savedCompany;
  }

  //
  public Page<Company> getCompanyByName(Pageable pageable, String companyName) {
      return companyRepository.findByCompanyNameContainingIgnoreCase(companyName, pageable);
  }


  // public Page<Company> getCompanyByName(Pageable pageable,String company_name) {
  //   return companyRepository.findByCompanyNameContainingIgnoreCase(company_name, pageable);
  // }

  public void deleteCompany(Long id) {
    companyRepository.deleteById(id);
  }

  public Company convertToEntity(CompanyDTO companyDTO) {
    return modelMapper.map(companyDTO, Company.class);
  }
}