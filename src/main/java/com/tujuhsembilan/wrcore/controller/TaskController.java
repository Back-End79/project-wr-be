package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.TaskDTO;
import com.tujuhsembilan.wrcore.service.TaskService;

import lib.i18n.utility.MessageUtil;
import lombok.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

    private final TaskService taskService;
    private final MessageUtil messageUtil;

    @PostMapping("/addTask")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) throws NotFoundException {
    try{
        var o = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonApiModelBuilder
                .jsonApiModel()
                .model(EntityModel.of(o))
                .meta("message",messageUtil.get("application.seccess.created","Task Data"))
                .build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", e.getMessage())
              .build());
    }
    }
    
}
