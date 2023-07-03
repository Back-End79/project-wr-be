package com.tujuhsembilan.wrcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Period;

public interface PeriodRepository extends JpaRepository<Period, Long>{
    
}
