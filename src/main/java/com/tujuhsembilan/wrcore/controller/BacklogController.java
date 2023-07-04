package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.BacklogDTO;
import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.service.BacklogService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/backlog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogController {
  private final BacklogService backlogService;
  private final MessageUtil messageUtil;

  @GetMapping
  public ResponseEntity<?> getAllBacklogs(
      Pageable pageable,
      @RequestParam(required = false, defaultValue = "taskName") String sortBy,
      @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
      @RequestParam(required = false, defaultValue = "") String search) {
    Page<Backlog> backlogPage;

    if (search != null || sortBy != null || sortDirection != null) {
      backlogPage = backlogService.searchAndSortBacklogs(search, sortBy, sortDirection, pageable);
    } else {
      backlogPage = backlogService.getAllBacklogs(pageable);
    }

    List<BacklogDTO> backlogDTOs = backlogPage.getContent().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());

    return ResponseEntity.ok(PagedModel.of(backlogDTOs,
        new PageMetadata(backlogPage.getSize(), backlogPage.getNumber(),
            backlogPage.getTotalElements())));
  }

  @PostMapping("/addBacklog")
  public ResponseEntity<?> createBacklog(@RequestBody BacklogDTO backlogDTO) {
    Backlog createdBacklog = backlogService.createBacklog(backlogDTO);
    BacklogDTO createdBacklogDTO = convertToDto(createdBacklog);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(createdBacklogDTO))
            .meta("message",
                messageUtil.get("application.success.created", "Backlog"))
            .build());
  }

  @GetMapping("/{backlogId}")
  public ResponseEntity<?> getBacklogById(@PathVariable Long backlogId) {
    try {
      Backlog backlog = backlogService.getBacklogById(backlogId);
      BacklogDTO backlogDTO = convertToDto(backlog);
      return ResponseEntity.ok(EntityModel.of(backlogDTO));

    } catch (NotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", "Data Not Found!")
              .build());

    }
  }

  @PutMapping("/{backlogId}")
  public ResponseEntity<?> updateBacklog(@PathVariable Long backlogId, @RequestBody BacklogDTO backlogDTO) {
    try {
      Backlog updatedBacklog = backlogService.updateBacklog(backlogId, backlogDTO);
      BacklogDTO updatedBacklogDTO = convertToDto(updatedBacklog);
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(updatedBacklogDTO))
              .meta("message", messageUtil.get("application.success.updated", "Backlog"))
              .build());

    } catch (NotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", "Data Not Found!")
              .build());

    }
  }

  @DeleteMapping("/{backlogId}")
  public ResponseEntity<?> deleteBacklog(@PathVariable Long backlogId) {
    try {
      backlogService.deleteBacklog(backlogId);
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel()
              .meta("message", messageUtil.get("application.success.deleted", "Backlog"))
              .build());

    } catch (NotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", "Data Not Found!")
              .build());

    }
  }

  private BacklogDTO convertToDto(Backlog backlog) {
    String fullName = backlog.getUserId().getFirstName() + " " + backlog.getUserId().getLastName();

    return BacklogDTO.builder()
        .backlogId(backlog.getBacklogId())
        .projectId(backlog.getProjectId().getProjectId())
        .statusBacklog(backlog.getStatusBacklog().getCategoryCodeId())
        .userId(backlog.getUserId().getUserId())
        .projectName(backlog.getProjectId().getPicProjectName())
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