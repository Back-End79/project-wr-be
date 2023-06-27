package com.tujuhsembilan.wrcore.ol.controller;

import com.tujuhsembilan.wrcore.service.CategoryCodeService;
import com.tujuhsembilan.wrcore.util.constant.CategoryCodeConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ol/divisionGroup")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DivisionGroupOLController {
    private final CategoryCodeService service;

    @GetMapping
    public ResponseEntity<?> olDivisionGroup(@RequestParam String search) {
        return ResponseEntity.ok(CollectionModel.of(service.findAllByCategoryNameAndSearch(CategoryCodeConstant.CATEGORY_DIVISION_GROUP,search)));
    }
}
