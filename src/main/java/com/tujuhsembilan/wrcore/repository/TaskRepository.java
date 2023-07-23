package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.model.Task;
import com.tujuhsembilan.wrcore.model.WorkingReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.workingReportId.userId.userId =:userId")
    List<Task> findTaskByUserId(@RequestParam("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.workingReportId.workingReportId =:wrId")
    Task findTaskByWrId(@RequestParam("wrId") Long wrId);

    @Query("SELECT t FROM Task t WHERE t.backlogId = :id AND t.workDate = :workDate AND t.isOvertime = true")
    Optional<Task> findBacklogIdAndWorkDate(Backlog id, Date workDate);

    Task findByBacklogIdAndWorkDate(Backlog backlog, LocalDate workDate);

    Optional<Task> findByWorkingReportId(WorkingReport workingReportId);

    List<Task> findByBacklogId(Backlog backlogId);

    // @Query("SELECT t FROM Task t WHERE t.taskId = :id AND t.isOvetime = true")
    // Optional<Task> findIsOvertime(Long id);

}
