package com.tujuhsembilan.wrcore.ol.controller;

import com.tujuhsembilan.wrcore.ol.dto.UserListDTO;
import com.tujuhsembilan.wrcore.service.UserListService;
import com.tujuhsembilan.wrcore.util.constant.CategoryCodeConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ol/userList")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserListController {
    private final UserListService userListService;

    @GetMapping
    public ResponseEntity<Object> getAllUserList(@RequestParam(required = false) String search) {
        List<UserListDTO> userListDto = userListService.getUserList(CategoryCodeConstant.CATEGORY_DIVISION_TALENT, search);
        return ResponseEntity.ok(CollectionModel.of(userListDto));
    }
}
