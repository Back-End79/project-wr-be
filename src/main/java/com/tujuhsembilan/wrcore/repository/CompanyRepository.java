package com.tujuhsembilan.wrcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  List<Company> findByCompanyNameContainingIgnoreCase(String company_name);

  Optional<Company> findByCompanyId(Long companyId);
}
