package com.tujuhsembilan.wrcore.ol.controller;

import com.tujuhsembilan.wrcore.service.TaskProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ol/taskProject")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskProjectController {
    private final TaskProjectService taskProjectService;

    @GetMapping
    public ResponseEntity<?> getTaskProject(@RequestParam Long projectId, @RequestParam String search) {
        return ResponseEntity.ok(CollectionModel.of(taskProjectService.getAllTaskProject(projectId, search)));
    }
}
