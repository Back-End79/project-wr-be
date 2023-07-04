package com.tujuhsembilan.wrcore.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

  public Page<Backlog> searchAndSortBacklogs(String search, String sortBy, Sort.Direction sortDirection,
      Pageable pageable) {
    Specification<Backlog> specification = searchByKeywordAndSort(search, sortBy, sortDirection);
    Page<Backlog> backlogPage = backlogRepository.findAll(specification, pageable);
    return backlogPage;
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

  // Function below for sorting and searching

  public Specification<Backlog> searchByKeyword(String keyword) {
    return (root, query, criteriaBuilder) -> {
      String likeKeyword = "%" + keyword.toLowerCase() + "%";

      Join<Backlog, Project> projectJoin = root.join("projectId");
      Predicate projectNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(projectJoin.get("picProjectName")),
          likeKeyword);

      Predicate taskNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("taskName")), likeKeyword);
      Predicate taskCodePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("taskCode")), likeKeyword);

      Join<Backlog, Users> userJoin = root.join("userId");
      Predicate userNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("userName")),
          likeKeyword);

      return criteriaBuilder.or(projectNamePredicate, taskNamePredicate, taskCodePredicate, userNamePredicate);
    };
  }

  public Sort sortBy(String sortBy, Sort.Direction sortDirection) {
    if (sortBy != null && sortDirection != null) {
      if (sortDirection.isAscending()) {
        return Sort.by(sortBy).ascending();
      } else {
        return Sort.by(sortBy).descending();
      }
    }
    return Sort.unsorted();
  }

  public Specification<Backlog> searchByKeywordAndSort(String keyword, String sortBy, Sort.Direction sortDirection) {
    Specification<Backlog> searchSpecification = searchByKeyword(keyword);
    Sort sort = sortBy(sortBy, sortDirection);

    return (root, query, criteriaBuilder) -> {
      Predicate searchPredicate = searchSpecification.toPredicate(root, query, criteriaBuilder);
      query.where(searchPredicate).orderBy(getOrder(root, query, criteriaBuilder, sort));
      return query.getRestriction();
    };
  }

  private List<Order> getOrder(Root<Backlog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Sort sort) {
    List<Order> orders = new ArrayList<>();
    if (sort != null) {
      for (Sort.Order sortOrder : sort) {
        String property = sortOrder.getProperty();
        if (sortOrder.isAscending()) {
          orders.add(criteriaBuilder.asc(root.get(property)));
        } else {
          orders.add(criteriaBuilder.desc(root.get(property)));
        }
      }
    }
    return orders;
  }

}
