package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.ol.dto.TaskProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BacklogRepository extends JpaRepository<Backlog, Long> {

  @Query(nativeQuery = true, value = "SELECT b.*, b.task_name AS taskName, b.task_code AS taskCode, " +
      "p.project_name AS projectName, cc.code_name AS status, u.user_name AS assignedTo " +
      "FROM backlog b " +
      "LEFT JOIN project p ON b.project_id = p.project_id " +
      "LEFT JOIN users u ON b.user_id = u.user_id " +
      "LEFT JOIN category_code cc ON b.status_backlog = cc.category_code_id " +
      "WHERE (:search IS NULL OR " +
      "LOWER(p.project_name) LIKE %:search% OR " +
      "LOWER(b.task_name) LIKE %:search% OR " +
      "LOWER(b.task_code) LIKE %:search% OR " +
      "LOWER(u.user_name) LIKE %:search% OR " +
      "LOWER(cc.code_name) LIKE %:search% OR " +
      "LOWER(b.priority) LIKE %:search%) " +
      "GROUP BY p.project_name, b.*, b.backlog_id, u.user_name, cc.code_name")
  Page<Backlog> findAllWithSearch(@Param("search") String search, Pageable pageable);

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.TaskProjectDTO(t) FROM Backlog t " +
      "WHERE t.projectId=:projectId " +
      "AND (:search IS NULL OR LOWER(t.taskName) LIKE %:search% OR LOWER(t.taskCode) LIKE %:search%)")
  List<TaskProjectDTO> findTaskProjectAll(Project projectId, String search);

  List<Backlog> findByProjectId(Project projectId);

}
