package com.tujuhsembilan.wrcore.controller;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.EmployeeDTO;
import com.tujuhsembilan.wrcore.dto.UserDTO;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.service.UserService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
  private final UserService userService;
  private final ModelMapper modelMapper;
  private final MessageUtil msg;

  @GetMapping
  public ResponseEntity<Object> getEmployee(Pageable pageable, @RequestParam(required = false) String search){
   Page<Users> users = userService.getAllEmployee(pageable, search);
   List<EmployeeDTO> employeeDTO = userService.convertToListEmployee(users);
   return ResponseEntity.ok(PagedModel.of(employeeDTO,
         new PageMetadata(users.getSize(), users.getNumber(), users.getTotalElements())));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
    Users user = userService.getUserById(userId);

    if (user != null) {
      UserDTO userDTO = buildUserDTO(user);

      return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(userDTO));
    }

    return ResponseEntity.notFound().build();
  }  
  
  @PostMapping("/addEmployee")
  public ResponseEntity<Object> addEmployee(@RequestBody UserDTO usersDto) {
    try {
        var o = userService.createEmployee(usersDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(JsonApiModelBuilder
                             .jsonApiModel()
                             .model(EntityModel.of(o))
                             .meta(ConstantMessage.MESSAGE,msg
                             .get("application.success.created","Employee"))
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

  @PutMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDto) {
    Users updatedUser = userService.updateUser(userId, userDto);

    if (updatedUser != null) {
      UserDTO updatedUserDTO = buildUserDTO(updatedUser);

      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel().model(EntityModel.of(updatedUserDTO))
              .meta(ConstantMessage.MESSAGE, msg.get("application.success.updated", "User")).build());
    }

    return ResponseEntity.notFound().build();
  }

  private UserDTO buildUserDTO(Users user) {
    return UserDTO.builder()
        .userId(user.getUserId())
        .jobTypeId(user.getJobTypeId().getCategoryCodeId())
        .nip(user.getNip())
        .placementType(user.getPlacementType().getCategoryCodeId())
        .ssoId(user.getSsoId())
        .group(user.getGroup().getCategoryCodeId())
        .position(user.getPosition().getCategoryCodeId())
        .statusOnsite(user.getStatusOnsite().getCategoryCodeId())
        .userName(user.getUserName())
        .email(user.getEmail())
        .lastContractStatus(user.getLastContractStatus())
        .lastContractDate(user.getLastContractDate())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .joinDate(user.getJoinDate())
        .endContractThisMonth(user.isEndContractThisMonth())
        .isActive(user.isActive())
        .photoProfile(user.getPhotoProfile())
        .lastModifiedOn(user.getLastModifiedOn())
        .lastModifiedBy(user.getLastModifiedBy())
        .createdBy(user.getCreatedBy())
        .createdOn(user.getCreatedOn())
        .placeOfBirth(user.getPlaceOfBirth())
        .dateOfBirth(user.getDateOfBirth())
        .identityNumber(user.getIdentityNumber())
        .postalCode(user.getPostalCode())
        .familyRelationship(user.getFamilyRelationship())
        .familyRelationshipNumber(user.getFamilyRelationshipNumber())
        .school(user.getSchool())
        .education(user.getEducation())
        .bpjsKesehatan(user.getBpjsKesehatan())
        .numberOfDependents(user.getNumberOfDependents())
        .bpjsClass(user.getBpjsClass())
        .carrerStartDate(user.getCarrerStartDate())
        .ptkpStatus(user.getPtkpStatus().getCategoryCodeId())
        .npwp(user.getNpwp())
        .no(user.getNo())
        .department(user.getDepartment())
        .build();
  }

  private EmployeeDTO convertToDto(Users users) {
    return modelMapper.map(users, EmployeeDTO.class);
  }

}
