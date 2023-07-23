package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.File;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File,Long> {
    @Query("SELECT f FROM File f WHERE f.workingReportId =:wrId")
    File getByWrId(@Param("wrId") WorkingReport wrId);
}
