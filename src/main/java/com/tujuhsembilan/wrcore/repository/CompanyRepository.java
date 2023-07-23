package com.tujuhsembilan.wrcore.repository;

import java.util.List;
import java.util.Optional;

import com.tujuhsembilan.wrcore.dto.DetailCompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tujuhsembilan.wrcore.model.Company;
import org.springframework.web.bind.annotation.RequestParam;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  @Query("SELECT c " +
     "FROM Company c " +
     "WHERE (:search IS NULL OR LOWER(c.companyName) LIKE %:search%) " +
     "AND c.isActive = true")
    Page<Company> findByCompanyName(@Param("search")String search, Pageable pageable);

  Optional<Company> findByCompanyId(Long companyId);

  @Query("SELECT c FROM Company c WHERE c.companyId = :id AND c.isActive = true")
  Optional<Company> findByIdAndActive(Long id);

  @Query("SELECT new com.tujuhsembilan.wrcore.dto.DetailCompanyDTO(c) FROM Company c " +
          "WHERE c.isActive = true " +
          "AND c.companyId = :companyId")
  DetailCompanyDTO getDetailCompany(Long companyId);

  @Query("SELECT c FROM Company c INNER JOIN Project p ON c.companyId = p.companyId " +
          "WHERE p.isActive=true AND c.companyId = :searchId")
  List<Company> listActiveProject (@RequestParam("searchId") Long searchId);
}
