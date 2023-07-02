package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.UserRoleDTO;
import com.tujuhsembilan.wrcore.service.UserRoleService;

import lombok.*;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRoleController {
  private final UserRoleService userRoleService;
  private final MessageUtil messageUtil;

  @PostMapping("/addUserRole")
  public ResponseEntity<?> addUserRole(@RequestBody UserRoleDTO userRoleDto) throws NotFoundException {
    try {
      var o = userRoleService.addUserRole(userRoleDto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(o))
              .meta("message", messageUtil
                  .get("application.success.created", "User Role"))
              .build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", e.getMessage())
              .build());
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateUserRole(@PathVariable("id") Long id, @RequestBody UserRoleDTO userRoleDto)
      throws NotFoundException {
    try {
      var o = userRoleService.updateUserRole(id, userRoleDto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(o))
              .meta("message", messageUtil
                  .get("application.success.updated"))
              .build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", e.getMessage())
              .build());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteUserRole(@PathVariable("id") Long id) {
    try {
      userRoleService.deleteRoleUser(id);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", messageUtil
                  .get("application.success.deleted"))
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
