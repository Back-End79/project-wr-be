package com.tujuhsembilan.wrcore.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.ProjectDTO;
import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.repository.CompanyRepository;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final CompanyRepository companyRepository;

  public Project createProject(ProjectDTO projectDTO) {
    Project project = convertToEntity(projectDTO);
    return projectRepository.save(project);
  }

  public Project getProjectById(Long projectId) {
    return projectRepository.findById(projectId).orElse(null);
  }

  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  public Project updateProject(Long projectId, ProjectDTO projectDTO) {
    Project existingProject = getProjectById(projectId);
    if (existingProject != null) {
      Project updatedProject = convertToEntity(projectDTO);
      updatedProject.setProjectId(projectId);
      return projectRepository.save(updatedProject);
    }
    return null;
  }

  public boolean deleteProject(Long projectId) {
    Project existingProject = getProjectById(projectId);
    if (existingProject != null) {
      projectRepository.delete(existingProject);
      return true;
    }
    return false;
  }

  private Project convertToEntity(ProjectDTO projectDTO) {
    return Project.builder()
        .companyId(companyRepository.findByCompanyId(projectDTO.getCompanyId()).orElseGet(() -> new Company()))
        .picProjectName(projectDTO.getPicProjectName())
        .picProjectPhone(projectDTO.getPicProjectPhone())
        .description(projectDTO.getDescription())
        .startDate(new Date(System.currentTimeMillis()))
        .endDate(new Date(System.currentTimeMillis()))
        .projectType(projectDTO.getProjectType())
        .build();
  }
}
