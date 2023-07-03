package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.WorkingReportDTO;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.service.WorkingReportService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workingReport")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkingReportController {
    
    private final WorkingReportService workingReportService;
    private final MessageUtil messageUtil;

    // @PostMapping("/addWorkingReport")
    // public ResponseEntity<?> addWorkingReport(@RequestBody WorkingReportDTO workingReportDTO) throws NotFoundException {
    //     try {
    //         var o = workingReportService.createWorkingReport(workingReportDTO);
    //         var u = buildWorkingReportDTO(o);
    //         return ResponseEntity.status(HttpStatus.CREATED)
    //                 .body(JsonApiModelBuilder
    //                         .jsonApiModel()
    //                         .model(EntityModel.of(u))
    //                         .meta("message", messageUtil.get("application.success.created", "Working Report"))
    //                         .build());
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //                 .body(JsonApiModelBuilder
    //                         .jsonApiModel()
    //                         .meta("message", e.getMessage())
    //                         .build());
    //     }
    // }

    // private WorkingReportDTO buildWorkingReportDTO(WorkingReport workingReport) {
    //     return WorkingReportDTO.builder()
    //             .workingReportId(workingReport.getWorkingReportId())
    //             .periodId(workingReport.getPeriodId().getPeriodId())
    //             .presenceId(workingReport.getPresenceId().getCategoryCodeId())
    //             .userId(workingReport.getUserId().getUserId())
    //             .date(workingReport.getDate())
    //             .checkIn(workingReport.getCheckIn())
    //             .checkOut(workingReport.getCheckOut())
    //             .workLocation(workingReport.getWorkLocation())
    //             .totalHours(workingReport.getTotalHours())
    //             .createTime(workingReport.getCreateTime())
    //             .lastModifiedTime(workingReport.getLastModifiedTime())
    //             .isHoliday(workingReport.isHoliday())
    //             .locationCheckin(workingReport.getLocationCheckin())
    //             .locationCheckout(workingReport.getLocationCheckout())
    //             .build();
    // }
}
