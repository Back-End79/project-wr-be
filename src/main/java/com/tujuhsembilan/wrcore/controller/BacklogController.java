package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public ResponseEntity<?> getAllBacklogs(Pageable pageable, @RequestParam(required = false) String search,
      @RequestParam(required = false) String sort) {
    Page<Backlog> backlogPage;

    if (search != null) {
      backlogPage = backlogService.getAllBacklogsWithSearch(pageable, search);
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

  @PostMapping
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
  public ResponseEntity<?> getBacklogById(@PathVariable Long backlogId) throws NotFoundException {
    Backlog backlog = backlogService.getBacklogById(backlogId);

    BacklogDTO backlogDTO = convertToDto(backlog);
    return ResponseEntity.ok(EntityModel.of(backlogDTO));
  }

  @PutMapping("/{backlogId}")
  public ResponseEntity<?> updateBacklog(@PathVariable Long backlogId,
      @RequestBody BacklogDTO backlogDTO)
      throws NotFoundException {
    Backlog updatedBacklog = backlogService.updateBacklog(backlogId, backlogDTO);
    BacklogDTO updatedBacklogDTO = convertToDto(updatedBacklog);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(updatedBacklogDTO))
            .meta("message",
                messageUtil.get("application.success.updated", "Backlog"))
            .build());
  }

  @DeleteMapping("/{backlogId}")
  public ResponseEntity<?> deleteBacklog(@PathVariable Long backlogId) throws NotFoundException {
    backlogService.deleteBacklog(backlogId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder.jsonApiModel()
            .meta("message", messageUtil.get("application.success.deleted", "Backlog")).build());
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
        .userName(fullName)
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
        .build();
  }
}