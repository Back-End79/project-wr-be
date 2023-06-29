package com.tujuhsembilan.wrcore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  Optional<Project> findByProjectId(Long projectId);
}
