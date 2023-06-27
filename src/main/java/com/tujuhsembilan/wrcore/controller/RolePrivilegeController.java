package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.RolePrivilegeDTO;
import com.tujuhsembilan.wrcore.model.RolePrivilege;
import com.tujuhsembilan.wrcore.service.RolePrivilegeService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rolePrivilege")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeController {
  private final RolePrivilegeService rolePrivilegeService;
  private final MessageUtil msg;

  @DeleteMapping("/{rolePrivilegeId}")
  public ResponseEntity<?> deleteRolePrivilege(@PathVariable Long rolePrivilegeId) throws NotFoundException {
    rolePrivilegeService.deleteRolePrivilege(rolePrivilegeId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder.jsonApiModel()
            .meta("message", msg.get("application.success.deleted", "Role Privilege")).build());
  }

  // @GetMapping
  //   public ResponseEntity<?> getAllRole(Pageable pageable) {
  //       int pageSize = 10;
  //       int pageNumber = pageable.getPageNumber();

  //       Pageable updatePageable = PageRequest.of(pageNumber, pageSize, pageable.getSort());
  //       Page<RolePrivilege> rolePrivilegePage = rolePrivilegeService.getAllRolePrivilege(updatePageable);
  //       Page<RolePrivilegeDTO> rolePrivilegeDtoPage = rolePrivilegePage.map(this::convertToDto);
  //       return ResponseEntity.ok(CollectionModel.of(rolePrivilegeDtoPage));
  //   }

  //   @PostMapping("/add")
  //   public ResponseEntity<?> addRolePrivilege(@RequestBody RolePrivilegeDTO rolePrivilegeDto) {
  //       var o = rolePrivilegeService.addRolePrivilege(rolePrivilegeDto);
  //       // var d = mdlMap.map(o, RolePrivilegeDto.class);
  //       return ResponseEntity.status(HttpStatus.CREATED)
  //                            .body(JsonApiModelBuilder
  //                            .jsonApiModel()
  //                            .model(EntityModel.of(o))
  //                            .meta("message",msg
  //                            .get("application.success.created","Role Privilege"))
  //                            .build());
  //   }
    
    // @PutMapping("/update/{id}")
    // public ResponseEntity<?> updateRolePrivilege(@PathVariable("id") Long id,
    //         @RequestBody RolePrivilegeDTO rolePrivilegeDTO) {
    //     // try {
    //         var o = rolePrivilegeService.updateRolePrivilege(id, rolePrivilegeDTO);
    //         // var d = mdlMap.map(o, RolePrivilegeDto.class);
    //        return ResponseEntity.status(HttpStatus.CREATED)
    //                          .body(JsonApiModelBuilder
    //                          .jsonApiModel()
    //                          .model(EntityModel.of(o))
    //                          .meta("message",msg
    //                          .get("application.success.Update","Role Privilege"))
    //                          .build());
    //     // } catch (IllegalArgumentException e) {
    //     //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     // }
    // }

    // private RolePrivilegeDTO convertToDto(RolePrivilege rolePrivilege) {
    //     RolePrivilegeDTO rolePrivilegeDto = new RolePrivilegeDTO();
    //     rolePrivilegeDto.setRoleId(rolePrivilege.getRoleId());
    //     rolePrivilegeDto.setPrivilegeId(rolePrivilege.getPrivilegeId());
    //     return rolePrivilegeDto;
    // }
  

  

  
}
