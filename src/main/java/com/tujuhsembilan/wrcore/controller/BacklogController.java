package com.tujuhsembilan.wrcore.controller;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.BacklogDTO;
import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.service.BacklogService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backlog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogController {
  private final BacklogService backlogService;
  private final MessageUtil messageUtil;

  @GetMapping
  public ResponseEntity<Object> getAllBacklogs(Pageable pageable, @RequestParam(required = false) String search) {
    Page<Backlog> backlogPage = backlogService.getAllBacklogs(pageable, search);
    List<BacklogDTO> backlogDTOs = backlogService.convertBacklogListDTOs(backlogPage);

    return ResponseEntity.ok(PagedModel.of(backlogDTOs,
        new PageMetadata(backlogPage.getSize(), backlogPage.getNumber(),
            backlogPage.getTotalElements())));
  }

  @PostMapping("/addBacklog")
  public ResponseEntity<Object> createBacklog(@RequestBody BacklogDTO backlogDTO) {
    BacklogDTO createdBacklog = backlogService.createBacklog(backlogDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(createdBacklog))
            .meta(ConstantMessage.MESSAGE, messageUtil.get("application.success.created", ConstantMessage.BACKLOG))
            .build());

  }

  @GetMapping("/{backlogId}")
  public ResponseEntity<Object> getBacklogById(@PathVariable Long backlogId) {
    BacklogDTO backlog = backlogService.getBacklogById(backlogId);
    return ResponseEntity.ok(EntityModel.of(backlog));
  }

  @PutMapping("/{backlogId}")
  public ResponseEntity<Object> updateBacklog(@PathVariable Long backlogId, @RequestBody BacklogDTO backlogDTO) {
    BacklogDTO updatedBacklog = backlogService.updateBacklog(backlogId, backlogDTO);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(updatedBacklog))
            .meta(ConstantMessage.MESSAGE, messageUtil.get("application.success.updated", ConstantMessage.BACKLOG))
            .build());
  }

  @DeleteMapping("/{backlogId}")
  public ResponseEntity<Object> deleteBacklog(@PathVariable Long backlogId) {

    backlogService.deleteBacklog(backlogId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder.jsonApiModel()
            .meta(ConstantMessage.MESSAGE, messageUtil.get("application.success.deleted", ConstantMessage.BACKLOG))
            .build());
  }

}