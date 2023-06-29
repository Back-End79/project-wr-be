package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.ProjectDTO;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.service.ProjectService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectController {

  private final ProjectService projectService;
  private final MessageUtil messageUtil;

  @PostMapping
  public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDto) {
    Project createdProject = projectService.createProject(projectDto);
    ProjectDTO createdProjectDTO = convertToDTO(createdProject);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(createdProjectDTO))
            .meta("message",
                messageUtil.get("application.success.created", "Project"))
            .build());
  }

  @GetMapping
  public ResponseEntity<?> getAllProjects() {
    List<Project> projects = projectService.getAllProjects();
    List<ProjectDTO> projectDTOs = projects.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(projectDTOs);
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(@PathVariable Long projectId) throws NotFoundException {
    Project project = projectService.getProjectById(projectId);

    if (project != null) {
      ProjectDTO projectDTO = convertToDTO(project);
      return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(projectDTO));
    }

    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectDTO projectDto)
      throws NotFoundException {
    Project updatedProject = projectService.updateProject(projectId, projectDto);

    if (updatedProject != null) {
      ProjectDTO updatedProjectDTO = convertToDTO(updatedProject);
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel().model(EntityModel.of(updatedProjectDTO))
              .meta("message", messageUtil.get("application.success.updated", "Project Detail")).build());
    }

    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
    boolean deleted = projectService.deleteProject(projectId);
    if (deleted) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel()
              .meta("message", messageUtil.get("application.success.deleted", "Project")).build());
    }
    return ResponseEntity.notFound().build();
  }

  public ProjectDTO convertToDTO(Project project) {
    return ProjectDTO.builder()
        .projectId(project.getProjectId())
        .companyId(project.getCompanyId().getCompanyId())
        .picProjectName(project.getPicProjectName())
        .picProjectPhone(project.getPicProjectPhone())
        .description(project.getDescription())
        .startDate(project.getStartDate())
        .endDate(project.getEndDate())
        .projectType(project.getProjectType())
        .build();
  }
}
