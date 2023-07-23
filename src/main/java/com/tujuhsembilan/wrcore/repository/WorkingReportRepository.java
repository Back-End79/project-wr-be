package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface WorkingReportRepository extends JpaRepository<WorkingReport, Long> {
  List<WorkingReport> findByDateAndUserId(Date date, Users user);

  @Query("SELECT wr FROM WorkingReport wr WHERE wr.userId.userId=:userId AND wr.date BETWEEN :startDate AND :endDate " +
          "ORDER BY wr.date")
  List<WorkingReport> findBetweenDate(@RequestParam("userId")Long userId, @RequestParam("startDate")Date startDate,
                                      @RequestParam("endDate")Date endDate);
  WorkingReport findByDate(Date date);

  @Query("SELECT wr FROM WorkingReport wr WHERE wr.isOvertime = false AND wr.date=:date")
  Optional<WorkingReport> findOptionalByDate(Date date);

  @Query("SELECT wr FROM WorkingReport wr WHERE wr.userId.userId =:userId")
  List<WorkingReport> findByUserId(@RequestParam("userId")Long userId);
}
