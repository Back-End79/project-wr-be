package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.BacklogDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeDetailDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeResponseDTO;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.service.OvertimeService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/overtime")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OvertimeController {

  private final MessageUtil messageUtil;
  private final OvertimeService overtimeService;

  @PostMapping("/addOvertime")
  public ResponseEntity<Object> createOvertime(
      @RequestBody OvertimeDTO overtimeDTO) {

    OvertimeResponseDTO overtime = overtimeService.createOvertime(overtimeDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(overtime))
            .meta(ConstantMessage.MESSAGE,
                messageUtil.get(ConstantMessage.APP_SUCCESS_CREATED, ConstantMessage.OVERTIME))
            .meta(ConstantMessage.EMAIL_MESSAGE, ConstantMessage.NOTIFICATION_EMAIL_SUCCESS)
            .build());
  }

  @GetMapping("/{workingReportId}")
  public ResponseEntity<Object> getBacklogById(@PathVariable Long workingReportId) {
    OvertimeDetailDTO workingReport = overtimeService.getOvertimeDetail(workingReportId);
    return ResponseEntity.ok(EntityModel.of(workingReport));
  }
}
