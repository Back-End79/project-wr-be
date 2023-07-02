package com.tujuhsembilan.wrcore.service;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.BacklogDTO;
import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.repository.BacklogRepository;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogService {

  private final BacklogRepository backlogRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  public Page<Backlog> getAllBacklogs(Pageable pageable) {
    return backlogRepository.findAll(pageable);
  }

  public Page<Backlog> getAllBacklogsWithSearch(Pageable pageable, String search) {
    return backlogRepository.findByTaskNameContainingIgnoreCase(search, pageable);
  }

  public Backlog createBacklog(BacklogDTO backlogDTO) {
    Project projectId = getProjectId(backlogDTO.getProjectId());
    CategoryCode statusBacklog = getCategoryCodeById(backlogDTO.getStatusBacklog());
    Users userId = getUserId(backlogDTO.getUserId());

    Backlog backlog = createBacklogMapper(backlogDTO, projectId, statusBacklog, userId);
    Backlog savedBacklog = backlogRepository.save(backlog);
    return savedBacklog;
  }

  public Backlog getBacklogById(Long backlogId) throws NotFoundException {
    return backlogRepository.findById(backlogId)
        .orElseThrow(() -> new NotFoundException());
  }

  public Backlog updateBacklog(Long backlogId, BacklogDTO backlogDTO) throws NotFoundException {
    Backlog existingBacklog = getBacklogById(backlogId);
    Project projectId = getProjectId(backlogDTO.getProjectId());
    CategoryCode statusBacklog = getCategoryCodeById(backlogDTO.getStatusBacklog());
    Users userId = getUserId(backlogDTO.getUserId());

    Backlog backlog = updateBacklogMapper(existingBacklog, backlogDTO, projectId, statusBacklog, userId);
    Backlog updatedBacklog = backlogRepository.save(backlog);

    return updatedBacklog;
  }

  public void deleteBacklog(Long backlogId) throws NotFoundException {
    Backlog existingBacklog = getBacklogById(backlogId);
    backlogRepository.delete(existingBacklog);
  }

  private CategoryCode getCategoryCodeById(Long categoryId) {
    return categoryCodeRepository.findByCategoryCodeId(categoryId)
        .orElseGet(() -> new CategoryCode());
  }

  private Project getProjectId(Long projectId) {
    return projectRepository.findByProjectId(projectId)
        .orElseGet(() -> new Project());
  }

  private Users getUserId(Long userId) {
    return userRepository.findByUserId(userId)
        .orElseGet(() -> new Users());
  }

  private Backlog createBacklogMapper(BacklogDTO backlogDTO, Project projectId, CategoryCode statusBacklog,
      Users userId) {
    return Backlog.builder()
        .projectId(projectId)
        .statusBacklog(statusBacklog)
        .userId(userId)
        .taskName(backlogDTO.getTaskName())
        .taskDescription(backlogDTO.getTaskDescription())
        .estimationTime(backlogDTO.getEstimationTime())
        .actualTime(backlogDTO.getActualTime())
        .estimationDate(new Date(System.currentTimeMillis()))
        .actualDate(new Date(System.currentTimeMillis()))
        .createdBy(backlogDTO.getCreatedBy())
        .updatedBy(backlogDTO.getUpdatedBy())
        .createdOn(new Timestamp(System.currentTimeMillis()))
        .updatedOn(new Timestamp(System.currentTimeMillis()))
        .priority(backlogDTO.getPriority())
        .build();
  }

  private Backlog updateBacklogMapper(Backlog existingBacklog, BacklogDTO backlogDTO, Project projectId,
      CategoryCode statusBacklog,
      Users userId) {
    return Backlog.builder()
        .backlogId(existingBacklog.getBacklogId())
        .projectId(projectId)
        .statusBacklog(statusBacklog)
        .userId(userId)
        .taskName(backlogDTO.getTaskName())
        .taskDescription(backlogDTO.getTaskDescription())
        .estimationTime(backlogDTO.getEstimationTime())
        .actualTime(backlogDTO.getActualTime())
        .estimationDate(existingBacklog.getEstimationDate())
        .actualDate(existingBacklog.getActualDate())
        .createdBy(backlogDTO.getCreatedBy())
        .updatedBy(backlogDTO.getUpdatedBy())
        .createdOn(existingBacklog.getCreatedOn())
        .updatedOn(new Timestamp(System.currentTimeMillis()))
        .priority(backlogDTO.getPriority())
        .build();
  }

}
