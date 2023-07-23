package com.tujuhsembilan.wrcore.controller;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.AddRolePrivilegeDTO;
import com.tujuhsembilan.wrcore.dto.DetailRolePrivilegeDTO;
import com.tujuhsembilan.wrcore.dto.UpdateRolePrivilegeDTO;
import com.tujuhsembilan.wrcore.service.RolePrivilegeService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/rolePrivilege")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeController {
  private final RolePrivilegeService rolePrivilegeService;
  private final MessageUtil msg;

  @GetMapping
  public ResponseEntity<Object> getAllRolePrivilege(Pageable pageable, @RequestParam(required = false) String search) {
    List<DetailRolePrivilegeDTO> listRoleDTO = rolePrivilegeService.convertToRolePrivilegeDTO(pageable, search);
    Page<DetailRolePrivilegeDTO> paging = new PageImpl<>(listRoleDTO);
    return ResponseEntity.ok(PagedModel.of(listRoleDTO,
          new PageMetadata(paging.getSize(), paging.getNumber(), paging.getTotalElements())));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getDetailRolePrivilege(@PathVariable("id") Long id) {
    try {
      var drp = rolePrivilegeService.getDetailRolePrivilege(id);
      return ResponseEntity.ok(EntityModel.of(drp));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta(ConstantMessage.MESSAGE, ex.getMessage())
              .build());
    }
  }

  @PostMapping("/addRolePrivilege")
  public ResponseEntity<Object> addRolePrivilege(@RequestBody AddRolePrivilegeDTO role){
    try {
      var o = rolePrivilegeService.createRolePrivilege(role);
      var d = rolePrivilegeService.convertToAddResponseRollPrivilege(o);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(d))
              .meta(ConstantMessage.MESSAGE, msg
                  .get("application.success.created", "User Role"))
              .build());
    } catch (Exception ex){
      log.info(ex.getMessage());
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(JsonApiModelBuilder
                           .jsonApiModel()
                           .meta(ConstantMessage.MESSAGE,ex.getMessage())
                           .build());
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Object> updateRolePrivilege(@PathVariable("id") Long id,
      @RequestBody UpdateRolePrivilegeDTO rolePrivilegeDTO) {
    try {
      rolePrivilegeService.updateRolePrivilege(id, rolePrivilegeDTO);
      var o = rolePrivilegeService.responseUpdateRolePrivilegeDTO(id);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(o))
              .meta(ConstantMessage.MESSAGE, msg
                  .get("application.success.Update", "Role Privilege"))
              .build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deleteRolePrivilege(@PathVariable("id") Long id) {
    try {
      rolePrivilegeService.deleteRolePrivilege(id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel()
              .meta(ConstantMessage.MESSAGE, msg
              .get("application.success.deleted", "Role Privilege"))
              .build());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(JsonApiModelBuilder
                           .jsonApiModel()
                           .meta(ConstantMessage.MESSAGE, ex.getMessage())
                           .build());
    }
  }
}
