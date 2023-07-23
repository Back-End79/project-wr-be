package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.Period;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
	@Query(value = "SELECT * FROM period " +
			"WHERE start_date = TO_DATE(?1, 'YYYY-MM-dd') ", nativeQuery = true)
	Period getPeriodByStartDate(String startDate);

	@Query("SELECT p FROM Period p WHERE :date BETWEEN p.startDate AND p.endDate")
	Period findByMonthYear(@Param("date") Date date);
}
