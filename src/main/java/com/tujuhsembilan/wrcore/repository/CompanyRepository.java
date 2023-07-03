package com.tujuhsembilan.wrcore.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  Page<Company> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
  // Page<Company> findAll(Pageable pageable);


  Optional<Company> findByCompanyId(Long companyId);
}
