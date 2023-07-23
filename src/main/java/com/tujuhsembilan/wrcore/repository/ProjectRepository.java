package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.dto.DetailCompanyListProjectDTO;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.ol.dto.ProjectOLDTO;
import com.tujuhsembilan.wrcore.ol.dto.ProjectTypeListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.ProjectOLDTO(project) FROM Project project " +
      "WHERE (:search IS NULL OR LOWER(project.projectName) LIKE %:search%)")
  List<ProjectOLDTO> findAllByProjectNameAndSearch(String search);

  Optional<Project> findByProjectId(Long projectId);

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.ProjectTypeListDTO(pr) FROM Project pr " +
      "INNER JOIN UserUtilization uut ON uut.projectId = pr.projectId " +
      "INNER JOIN Users u ON uut.userId = u.userId " +
      "WHERE (u.isActive= true) AND (pr.isActive= true) " +
      "AND (:search IS NULL OR LOWER(pr.projectName) LIKE %:search%) " +
      "AND (uut.isActive= true) AND uut.userId =:userId")
  List<ProjectTypeListDTO> findAllProjectType(@RequestParam("userId") Users userId, String search);

  @Query("SELECT new com.tujuhsembilan.wrcore.dto.DetailCompanyListProjectDTO(p) FROM Project p " +
      "JOIN CategoryCode cc ON p.projectType = cc AND p.companyId.companyId =:companyId AND p.isActive=true ")
  List<DetailCompanyListProjectDTO> getDetailCompanyListProject(Long companyId);

  @Query("SELECT p, c.companyName AS clientName FROM Project p JOIN Company c ON c.companyId = p.companyId " +
      "WHERE (:search IS NULL OR LOWER(p.projectName) LIKE %:search% OR LOWER(c.companyName) LIKE %:search%) " +
      "AND p.isActive = true")
  Page<Object[]> findByProjectName(String search, Pageable pageable);

  @Query("SELECT p FROM Project p WHERE p.projectId = :id AND p.isActive = true")
  Optional<Project> findByIdAndActive(Long id);

  @Query("SELECT CASE WHEN COUNT(cc) > 0 THEN TRUE ELSE FALSE END FROM CategoryCode cc " + 
         "WHERE cc.categoryCodeId = :id AND (cc.codeName IN ('Administrator', 'HRD', 'Team Lead of Project'))")
  boolean hasValidRoleForAddProject(Long id);

  @Query("SELECT CASE WHEN COUNT(cc) > 0 THEN TRUE ELSE FALSE END FROM CategoryCode cc " + 
         "WHERE cc.categoryCodeId = :id AND (cc.codeName IN ('Administrator', 'HRD', 'Team Lead of Project', 'Maintainer', 'Master'))")
  boolean hasValidRoleForUpdateProject(Long id);
  
}