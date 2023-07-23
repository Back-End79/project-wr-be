package com.tujuhsembilan.wrcore.ol.controller;

import com.tujuhsembilan.wrcore.ol.dto.ProjectTypeListDTO;
import com.tujuhsembilan.wrcore.service.ProjectTypeListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("ol/projectTypeList")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectTypeListOLController {
    public final ProjectTypeListService projectTypeListService;

    @GetMapping
    public ResponseEntity<?> olProjectList(@RequestParam Long userId, @RequestParam(required = false) String search) {
        List<ProjectTypeListDTO> projectTypeListDTO = projectTypeListService.getListProjectOfUser(userId, search);
        return ResponseEntity.ok(CollectionModel.of(projectTypeListDTO));
    }
}
