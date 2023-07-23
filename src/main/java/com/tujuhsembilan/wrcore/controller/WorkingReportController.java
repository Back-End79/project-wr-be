package com.tujuhsembilan.wrcore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.*;
import com.tujuhsembilan.wrcore.exporters.WorkingReportExcelExporter;
import com.tujuhsembilan.wrcore.exporters.WorkingReportPDFExporter;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.repository.TaskRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;
import com.tujuhsembilan.wrcore.service.CheckoutEmployeeService;
import com.tujuhsembilan.wrcore.service.WorkingReportService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/workingReport")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkingReportController {
  private final WorkingReportService workingReportService;
  private final WorkingReportRepository workingReportRepository;
  private final CheckoutEmployeeService checkoutEmployeeService;
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final MessageUtil messageUtil;


  @GetMapping("/{startDate}/{endDate}/{userId}")
  public ResponseEntity<Object> generateWorkingReport(
          @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
          @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
          @PathVariable("userId") Long userId) {

    List<WorkingReportDTO> workingReports = workingReportService.generateWorkingReport(startDate, endDate, userId);

    return ResponseEntity.ok(CollectionModel.of(workingReports));
  }

  @PostMapping("/attendance")
  public ResponseEntity<Object> checkInAttendance(@RequestBody EmployeeAttendanceDTO body) {
    var o = workingReportService.checkInAttendance(body);
    var c = modelMapper.map(o, EmployeeAttendanceResponseDTO.class);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(JsonApiModelBuilder
                    .jsonApiModel()
                    .model(EntityModel.of(c))
                    .meta(ConstantMessage.MESSAGE, messageUtil
                            .get(ConstantMessage.APP_SUCCESS_CREATED, ConstantMessage.WORKING_REPORT))
                    .build());
  }

  @PostMapping("/notAttendance")
  public ResponseEntity<Object> checkInAttendance(@RequestBody EmployeeNotAttendanceDTO body) {
    var o = workingReportService.checkInNotAttendance(body);
    var c = modelMapper.map(o, EmployeeAttendanceResponseDTO.class);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(JsonApiModelBuilder
                    .jsonApiModel()
                    .model(EntityModel.of(c))
                    .meta(ConstantMessage.MESSAGE, messageUtil
                            .get(ConstantMessage.APP_SUCCESS_CREATED, ConstantMessage.WORKING_REPORT))
                    .build());
  }

  @PostMapping("/attendance/checkIn")
  public ResponseEntity<Object> checkIn(@RequestBody CheckInDTO body) {
    var o = workingReportService.checkIn(body);
    var c = modelMapper.map(o, EmployeeAttendanceResponseDTO.class);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(JsonApiModelBuilder
                    .jsonApiModel()
                    .model(EntityModel.of(c))
                    .meta(ConstantMessage.MESSAGE, messageUtil
                            .get(ConstantMessage.APP_SUCCESS_CREATED, ConstantMessage.WORKING_REPORT))
                    .build());
  }

  @PutMapping("/checkOut")
  public ResponseEntity<Object> checkoutEmployee(@RequestParam Long wrId, @RequestParam String fileName, @RequestParam String latitude, @RequestParam String longitude) {
    try {
      checkoutEmployeeService.checkoutEmployee(wrId, latitude, longitude);
      checkoutEmployeeService.uploadedPhoto(wrId, fileName);
      return ResponseEntity.status(HttpStatus.OK)
              .body(JsonApiModelBuilder
                      .jsonApiModel()
                      .meta(ConstantMessage.MESSAGE, messageUtil
                              .get("CheckOut success! File Created!"))
                      .build());
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.getStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(JsonApiModelBuilder
                      .jsonApiModel()
                      .meta(ConstantMessage.MESSAGE, ex.getMessage())
                      .build());
    }
  }

  @GetMapping("/download/pdf")
  public void downloadToPdf(HttpServletResponse response, @RequestParam("userId") Long userId,
                            @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate,
                            @Nullable @RequestParam(name = "configuration") String config, @RequestParam(name = "approver") String approver)
          throws DocumentException, IOException, ParseException {

    ExportConfigurationDTO cfg;
    if (config != null && !config.trim().isEmpty()) {
      config = new String(Base64.decodeBase64(config), StandardCharsets.UTF_8);
      ObjectMapper objMap = new ObjectMapper();
      cfg = objMap.readValue(config, ExportConfigurationDTO.class);
    } else {
      cfg = new ExportConfigurationDTO();
    }

    response.setContentType("application/pdf");
    DateFormat dateFormatter = new SimpleDateFormat("MMMM yyyy");
    SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM");
    String currentDateTime = dateFormatter.format(new Date());

    String[] arrDate = startDate.split("-");
    String titleDate = arrDate[0] + "-" + arrDate[1];
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    Date starts = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date ends = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());

    Date date = dateFormatter2.parse(titleDate);
    String dateTitle = dateFormatter.format(date);

    Users users = userRepository.findById(userId).get();

    List<WorkingReport> listWorkingReportByUser = workingReportRepository.findBetweenDate(userId, starts, ends);
    String userName = users.getUserName();

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=Timesheet - " + userName + " - " + currentDateTime + ".pdf";
    response.setHeader(headerKey, headerValue);

    WorkingReportPDFExporter exporter = new WorkingReportPDFExporter(listWorkingReportByUser, cfg);
    exporter.export(response, userName, taskRepository.findTaskByUserId(userId), dateTitle, approver);
  }

  @GetMapping("/download/excel")
  public void exportToExcel(HttpServletResponse response, @RequestParam("userId") Long userId,
                            @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate,
                            @Nullable @RequestParam(name = "configuration") String config) throws IOException {
    ExportConfigurationDTO cfg;
    if (config != null && !config.trim().isEmpty()) {
      config = new String(Base64.decodeBase64(config), StandardCharsets.UTF_8);
      ObjectMapper objMap = new ObjectMapper();
      cfg = objMap.readValue(config, ExportConfigurationDTO.class);
    } else {
      cfg = new ExportConfigurationDTO();
    }

    response.setContentType("application/octet-stream");

    DateFormat dateFormatter = new SimpleDateFormat("MMMM yyyy");
    String currentDateTime = dateFormatter.format(new Date());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    Date starts = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date ends = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());

    List<WorkingReport> listWorkingReport = workingReportRepository.findBetweenDate(userId, starts, ends);

    String userName = userRepository.findById(userId).get().getUserName();

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=Timesheet - " + userName + " - " + currentDateTime + ".xlsx";
    response.setHeader(headerKey, headerValue);

    WorkingReportExcelExporter excelExporter = new WorkingReportExcelExporter(listWorkingReport, cfg);
    excelExporter.export(response, userName, taskRepository.findTaskByUserId(userId));
  }

  @GetMapping("download/{reportFormat}/{userId}")
  public @ResponseBody void downloadReport(HttpServletResponse response, @PathVariable String reportFormat,
                                           @PathVariable Long userId) throws JRException, IOException {
    workingReportService.download(response, reportFormat, userId);
  }

}
