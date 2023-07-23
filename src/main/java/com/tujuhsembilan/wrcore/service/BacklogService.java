package com.tujuhsembilan.wrcore.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogService {

  private final BacklogRepository backlogRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final Validator validator;

  public Page<Backlog> getAllBacklogs(Pageable pageable, String search) {
    Page<Backlog> backlogs = backlogRepository.findAllWithSearch(search, pageable);
    return backlogs;
  }

  public List<BacklogDTO> convertBacklogListDTOs(Page<Backlog> backlogs) {
    List<BacklogDTO> backlogDTOs = backlogs.getContent().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());

    return backlogDTOs;
  }

  public BacklogDTO getBacklogById(Long backlogId) {
    Backlog backlog = backlogRepository.findById(backlogId)
        .orElseThrow(() -> new EntityNotFoundException("Backlog Not Found"));

    BacklogDTO backlogDTO = convertToDto(backlog);
    return backlogDTO;
  }

  @Transactional
  public BacklogDTO createBacklog(BacklogDTO backlogDTO) {
    validateBacklogDTO(backlogDTO);

    try {
      Project projectId = getProjectId(backlogDTO.getProjectId());
      CategoryCode statusBacklog = getCategoryCodeById(backlogDTO.getStatusBacklog());
      Users userId = getUserId(backlogDTO.getUserId());

      Backlog backlog = createBacklogMapper(backlogDTO, projectId, statusBacklog, userId);
      Backlog savedBacklog = backlogRepository.save(backlog);

      BacklogDTO savedbacklogDTO = convertToDto(savedBacklog);
      return savedbacklogDTO;

    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  private void validateBacklogDTO(BacklogDTO backlogDTO) {
    Set<ConstraintViolation<BacklogDTO>> violations = validator.validate(backlogDTO);
    if (!violations.isEmpty()) {
      List<String> errors = violations.stream()
          .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
          .collect(Collectors.toList());
      throw new ValidationException("Validation Errors : " + errors);
    }
  }

  @Transactional
  public BacklogDTO updateBacklog(Long backlogId, BacklogDTO backlogDTO) {
    try {
      Backlog existingBacklog = backlogRepository.findById(backlogId)
          .orElseThrow(() -> new EntityNotFoundException("Backlog Not Found"));

      Project projectId = getProjectId(backlogDTO.getProjectId());
      CategoryCode statusBacklog = getCategoryCodeById(backlogDTO.getStatusBacklog());
      Users userId = getUserId(backlogDTO.getUserId());

      Backlog backlog = updateBacklogMapper(existingBacklog, backlogDTO, projectId, statusBacklog, userId);
      Backlog updatedBacklog = backlogRepository.save(backlog);

      BacklogDTO updatedBacklogDTO = convertToDto(updatedBacklog);
      return updatedBacklogDTO;

    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  @Transactional
  public void deleteBacklog(Long backlogId) {
    try {
      Backlog existingBacklog = backlogRepository.findById(backlogId)
          .orElseThrow(() -> new EntityNotFoundException("Backlog Not Found"));

      backlogRepository.delete(existingBacklog);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  private CategoryCode getCategoryCodeById(Long categoryId) {
    return categoryCodeRepository.findByCategoryCodeId(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Category Code Not Found"));
  }

  private Project getProjectId(Long projectId) {
    return projectRepository.findByProjectId(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project Not Found"));
  }

  private Users getUserId(Long userId) {
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
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
        .taskCode(backlogDTO.getTaskCode())
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
        .estimationDate(backlogDTO.getEstimationDate())
        .actualDate(backlogDTO.getActualDate())
        .createdBy(existingBacklog.getCreatedBy())
        .updatedBy(backlogDTO.getUpdatedBy())
        .createdOn(existingBacklog.getCreatedOn())
        .updatedOn(new Timestamp(System.currentTimeMillis()))
        .priority(backlogDTO.getPriority())
        .taskCode(backlogDTO.getTaskCode())
        .build();
  }

  private BacklogDTO convertToDto(Backlog backlog) {
    String fullName = backlog.getUserId().getFirstName() + " " + backlog.getUserId().getLastName();

    return BacklogDTO.builder()
        .backlogId(backlog.getBacklogId())
        .projectId(backlog.getProjectId().getProjectId())
        .statusBacklog(backlog.getStatusBacklog().getCategoryCodeId())
        .userId(backlog.getUserId().getUserId())
        .projectName(backlog.getProjectId().getProjectName())
        .status(backlog.getStatusBacklog().getCodeName())
        .assignedTo(fullName)
        .taskName(backlog.getTaskName())
        .taskDescription(backlog.getTaskDescription())
        .estimationTime(backlog.getEstimationTime())
        .actualTime(backlog.getActualTime())
        .estimationDate(backlog.getEstimationDate())
        .actualDate(backlog.getActualDate())
        .createdBy(backlog.getCreatedBy())
        .updatedBy(backlog.getUpdatedBy())
        .createdOn(backlog.getCreatedOn())
        .updatedOn(backlog.getUpdatedOn())
        .priority(backlog.getPriority())
        .taskCode(backlog.getTaskCode())
        .build();
  }

}
