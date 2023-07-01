package com.tujuhsembilan.wrcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.ol.dto.OLProjectDTO;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.OLProjectDTO(project) FROM Project project " +
      "WHERE (:search IS NULL OR LOWER(project.picProjectName) LIKE %:search%)")
  List<OLProjectDTO> findAllByProjectNameAndSearch(String search);

  Optional<Project> findByProjectId(Long projectId);
}
