package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(value = "roleId", required = false) Long roleId, Pageable pageable) {
        
      int pageSize = 10;
      int pageNumber = pageable.getPageNumber();
      Pageable updatedPageable = PageRequest.of(pageNumber, pageSize, pageable.getSort());

      if (roleId != null) {
          List<RolePrivilege> rolePrivilege = rolePrivilegeService.getRoleById(roleId);
          List<RolePrivilegeDTO> rolePrivilegeDTO = rolePrivilege.stream()
              .map(this::buildRolePrivilegeDTO)
              .collect(Collectors.toList());
          return ResponseEntity.ok(CollectionModel.of(rolePrivilegeDTO));
      } else {
          Page<RolePrivilege> rolePrivilePage = rolePrivilegeService.getAllRolePrivilege(updatedPageable);
          Page<RolePrivilegeDTO> rolePrivilegeDTO = rolePrivilePage.map(this::buildRolePrivilegeDTO);
        //   return ResponseEntity.ok(CollectionModel.of(rolePrivilegeDTO));
          return ResponseEntity.ok(rolePrivilegeDTO);
      }
  }

  private RolePrivilegeDTO buildRolePrivilegeDTO(RolePrivilege rolePrivilege) {
    return RolePrivilegeDTO.builder()
            .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
            .privilegeId(rolePrivilege.getPrivilegeId().getCategoryCodeId())
            .roleId(rolePrivilege.getRoleId().getCategoryCodeId())
            .build();
  }

  @DeleteMapping("/{rolePrivilegeId}")
  public ResponseEntity<?> deleteRolePrivilege(@PathVariable Long rolePrivilegeId) throws NotFoundException {
    rolePrivilegeService.deleteRolePrivilege(rolePrivilegeId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder.jsonApiModel()
            .meta("message", msg.get("application.success.deleted", "Role Privilege")).build());
  }

    @PostMapping("/addRolePrivilege")
    public ResponseEntity<?> addRolePrivilege(@RequestBody RolePrivilegeDTO rolePrivilegeDto) throws NotFoundException {
        var o = rolePrivilegeService.createRolePrivilege(rolePrivilegeDto);
        // var d = mdlMap.map(o, RolePrivilegeDto.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(JsonApiModelBuilder
                                .jsonApiModel()
                                .model(EntityModel.of(o))
                                .meta("message",msg
                                .get("application.success.created","Role Privilege"))
                                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRolePrivilege(@PathVariable("id") Long id, @RequestBody RolePrivilegeDTO rolePrivilegeDTO) throws NotFoundException {
        try {
            var o = rolePrivilegeService.updateRolePrivilege(id, rolePrivilegeDTO);
            // var d = mdlMap.map(o, RolePrivilegeDto.class);
            return ResponseEntity.status(HttpStatus.CREATED)
                                .body(JsonApiModelBuilder
                                .jsonApiModel()
                                .model(EntityModel.of(o))
                                .meta("message",msg
                                .get("application.success.Update","Role Privilege"))
                                .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

  

  

  
}
