package com.tujuhsembilan.wrcore.ol.controller;

import com.tujuhsembilan.wrcore.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ol/project")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectOLController {

  private final ProjectService service;

  @GetMapping
  public ResponseEntity<?> olProject(@RequestParam(required = false) String search) {
    return ResponseEntity.ok(
        CollectionModel.of(service.findAllByProjectNameAndSearch(search)));
  }
}
