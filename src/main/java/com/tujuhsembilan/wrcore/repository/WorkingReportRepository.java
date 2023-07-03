package com.tujuhsembilan.wrcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tujuhsembilan.wrcore.model.WorkingReport;

@Repository
public interface WorkingReportRepository extends JpaRepository<WorkingReport, Long>{
    
}
