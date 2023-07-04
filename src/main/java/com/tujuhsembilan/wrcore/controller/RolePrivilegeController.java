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
import org.springframework.web.bind.annotation.*;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.RolePrivilegeDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.RolePrivilege;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.service.RolePrivilegeService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rolePrivilege")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeController {
  private final RolePrivilegeService rolePrivilegeService;
  private final MessageUtil msg;
  private final CategoryCodeRepository categoryCodeRepository;
@GetMapping
    public ResponseEntity<?> getAllRolePrivilege(Pageable pageable, @RequestParam(required = false) String search) {

        Page<RolePrivilege> rolePrivilegePage;

        if (search != null && !search.isEmpty()) {
            rolePrivilegePage = rolePrivilegeService.getRoleById(search, pageable);
        } else {
            rolePrivilegePage = rolePrivilegeService.getAllRolePrivilege(pageable);
        }

        List<RolePrivilegeDTO> rolePrivilegeDTO = rolePrivilegePage.getContent().stream()
                .map(this::buildRolePrivilegeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagedModel.of(rolePrivilegeDTO,
                new PageMetadata(rolePrivilegePage.getSize(), rolePrivilegePage.getNumber(),
                        rolePrivilegePage.getTotalElements())));
    }

    private RolePrivilegeDTO buildRolePrivilegeDTO(RolePrivilege rolePrivilege) {
        RolePrivilegeDTO rolePrivilegeDTO = RolePrivilegeDTO.builder()
                .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
                .privilegeId(rolePrivilege.getPrivilegeId().getCategoryCodeId())
                .roleId(rolePrivilege.getRoleId().getCategoryCodeId())
                .build();

        try {
            CategoryCode privilegeCategory = categoryCodeRepository.findById(rolePrivilege.getPrivilegeId().getCategoryCodeId()).orElseThrow(() -> new NotFoundException());
            CategoryCode roleCategory = categoryCodeRepository.findById(rolePrivilege.getRoleId().getCategoryCodeId()).orElseThrow(() -> new NotFoundException());

            if (privilegeCategory != null) {
                rolePrivilegeDTO.setPrivilegeCategoryName(privilegeCategory.getCategoryName());
            }

            if (roleCategory != null) {
                rolePrivilegeDTO.setRoleCategoryName(roleCategory.getCategoryName());
            }
        } catch (NotFoundException e) {
            rolePrivilegeDTO.setPrivilegeCategoryName(null);
            rolePrivilegeDTO.setRoleCategoryName(null);
        }

        return rolePrivilegeDTO;
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
