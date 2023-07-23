package com.tujuhsembilan.wrcore.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

  Holiday findByDate(Date date);
  Optional<Holiday> findOptionalByDate(Date date);

}
