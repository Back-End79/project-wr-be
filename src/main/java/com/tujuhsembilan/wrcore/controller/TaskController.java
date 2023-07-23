// package com.tujuhsembilan.wrcore.controller;

// import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
// import com.tujuhsembilan.wrcore.dto.AddTaskDTO;
// import com.tujuhsembilan.wrcore.service.TaskService;
// import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
// import lib.i18n.utility.MessageUtil;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/task")
// @RequiredArgsConstructor(onConstructor = @__(@Autowired))
// public class TaskController {

// private final TaskService taskService;
// private final MessageUtil messageUtil;

// @PostMapping("/addTask")
// public ResponseEntity<Object> createTask(@RequestBody AddTaskDTO taskDTO) {
// var v = taskService.createTask(taskDTO);
// var o = taskService.listItemTask(v);
// return ResponseEntity.status(HttpStatus.CREATED)
// .body(JsonApiModelBuilder
// .jsonApiModel()
// .model(EntityModel.of(o))
// .meta(ConstantMessage.MESSAGE, messageUtil.get("application.success.created",
// "Task"))
// .build());
// }

// }
