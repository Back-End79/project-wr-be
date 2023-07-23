package com.tujuhsembilan.wrcore.controller;


import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.AddUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.DetailUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.UpdateUserRoleDTO;
import com.tujuhsembilan.wrcore.model.UserRole;
import com.tujuhsembilan.wrcore.service.UserRoleService;
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

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRoleController {
  private final UserRoleService userRoleService;
  private final MessageUtil messageUtil;

  @GetMapping
  public ResponseEntity<Object> getAllUserRole(Pageable pageable, @RequestParam(required = false) String search) {
    List<DetailUserRoleDTO> listUserRole = userRoleService.convertToRolePrivilegeDTO(pageable,search);
    Page<DetailUserRoleDTO> userRole = new PageImpl<>(listUserRole);
    return ResponseEntity.ok(PagedModel.of(listUserRole, 
          new PageMetadata(userRole.getSize(), userRole.getNumber(), userRole.getTotalElements())));
  }
  @GetMapping("/{id}")
  public ResponseEntity<Object> getDetailUserRole(@PathVariable("id") Long id) {
    try{
      var o = userRoleService.getDetailUserRole(id);    
      return ResponseEntity.ok(EntityModel.of(o));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta(ConstantMessage.MESSAGE, ex.getMessage())
              .build());
    }
  }  
 
  @PostMapping("/addUserRole")
  public ResponseEntity<Object> addUserRole(@RequestBody AddUserRoleDTO userRoleDto) {
    try {
      var o = userRoleService.addUserRole(userRoleDto);
      var d = userRoleService.convertAddUserRoleDTO(o);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(d))
              .meta(ConstantMessage.MESSAGE, messageUtil
                  .get("application.success.created", "User Role"))
              .build());
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(JsonApiModelBuilder
                             .jsonApiModel()
                             .meta(ConstantMessage.MESSAGE, ex.getMessage())
                             .build());
    }
  }
  
  @PutMapping("/update/{id}")
  public ResponseEntity<Object> updateUserRole(@PathVariable("id") Long id, @RequestBody UpdateUserRoleDTO userRoleDto) {
    try {
      userRoleService.updateUserRole(id, userRoleDto);
      var d = userRoleService.convertUpdateUserRoleDTO(id);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(d))
              .meta(ConstantMessage.MESSAGE, messageUtil
                  .get("application.success.updated"))
              .build());
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(JsonApiModelBuilder
                             .jsonApiModel()
                             .meta(ConstantMessage.MESSAGE, ex.getMessage())
                             .build());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deleteUserRole(@PathVariable("id") Long id) {
    try {
      userRoleService.deleteRoleUser(id);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta(ConstantMessage.MESSAGE, messageUtil
                  .get("application.success.deleted"))
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
