package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.dto.*;
import com.tujuhsembilan.wrcore.model.*;
import com.tujuhsembilan.wrcore.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Table;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class WorkingReportService {
  final private String tableName = WorkingReport.class.getAnnotation(Table.class).name();
  final private String jrxmlPath = "classpath:" + tableName + ".jrxml";

  private final HolidayRepository holidayRepository;
  private final PeriodRepository periodRepository;
  private final WorkingReportRepository workingReportRepository;
  private final UserRepository userRepository;
  private final FileRepository fileRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final TaskRepository taskRepository;

  /**
   * Generates a list of working reports for a specified period and user.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @param userId    The ID of the user.
   * @return A list of WorkingReportDTO objects.
   */
  public List<WorkingReportDTO> generateWorkingReport(LocalDate startDate, LocalDate endDate, Long userId) {
    List<WorkingReportDTO> workingReports = new ArrayList<>();

    AtomicReference<Users> users = new AtomicReference<>(new Users());
    userRepository.findByUserId(userId).ifPresentOrElse(users::set,
            () -> { throw new EntityNotFoundException("User Not Found"); });

    LocalDate actualEndDate = endDate.plusDays(1);
    for (LocalDate date = startDate; date.isBefore(actualEndDate); date = date.plusDays(1)) {

      WorkingReportDTO dto = new WorkingReportDTO();
      dto.setId(String.valueOf(date.toEpochDay()));

      // add data user
      dto.setName(users.get().getFirstName() + " " + users.get().getLastName());
      dto.setRoleName(users.get().getPosition().getCategoryName());
      dto.setUserId(userId);

      ListDateDTO listDateDTO = new ListDateDTO();
      listDateDTO.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));

      // check if the date is holiday
      LocalDate finalDate = date;
      holidayRepository.findOptionalByDate(java.sql.Date.valueOf(date))
              .ifPresentOrElse(holiday -> {
        listDateDTO.setHoliday(true);
        listDateDTO.setDescHoliday(holiday.getNotes());
      }, () -> {

        // check if the date is weekend
        if (finalDate.getDayOfWeek().toString().equals("SATURDAY") || finalDate.getDayOfWeek().toString().equals("SUNDAY")) {
          listDateDTO.setHoliday(true);
          listDateDTO.setDescHoliday("Weekend");
        } else {

          // check if has attendance
          workingReportRepository.findOptionalByDate(java.sql.Date.valueOf(finalDate))
                  .ifPresent(workingReport -> {
                    listDateDTO.setCheckIn(!Objects.isNull(workingReport.getCheckIn()));
                    listDateDTO.setWorkingReportId(workingReport.getWorkingReportId());
                    listDateDTO.setPresenceId(workingReport.getPresenceId().getCategoryCodeId());
                    listDateDTO.setPresenceName(workingReport.getPresenceId().getCategoryName());
                    listDateDTO.setOvertime(workingReport.isOvertime());
                    dto.setPeriod(workingReport.getPeriodId().getPeriodId());
                    // idk
                    listDateDTO.setTask(true);
                  });
        }
        if (dto.getPeriod()==null) {

          dto.setPeriod(periodRepository.findByMonthYear(
                  Date.from(finalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())).getPeriodId());
        }
        Optional<Task> task = Optional.ofNullable(taskRepository.findTaskByWrId(listDateDTO.getWorkingReportId()));
        if (task.isPresent()) {
          listDateDTO.setTaskId(task.get().getTaskId());
        } else {
          listDateDTO.setTaskId(null);
        }
        dto.setListDate(listDateDTO);
        workingReports.add(dto);
      });
    }

    return workingReports;
  }

  /**
   * Retrieves the period ID for the given date.
   *
   * @param date The date.
   * @return The period ID.
   */
  private Long getPeriodId(Date date) {
    Period period = periodRepository.findByMonthYear(date);
    if (period != null) {
      return period.getPeriodId();
    }
    return null;
  }

  /**
   * Checks if a date is a holiday.
   *
   * @param date The date to check.
   * @return True if the date is a holiday, false otherwise.
   */
  private boolean checkIfDateIsHoliday(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Check if the date falls on a Saturday or Sunday
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
        calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      return true;
    }

    List<Date> holidayDates = getHolidayDates();

    for (Date holidayDate : holidayDates) {
      if (isSameDay(date, holidayDate)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if the user has a check-in record for the given date.
   *
   * @param date   The date to check.
   * @param userId The ID of the user.
   * @return True if the user has a check-in record, false otherwise.
   */
  private boolean checkIfUserHasCheckin(Date date, Long userId) {
    Users user = getUserId(userId);
    List<WorkingReport> userWorkingReports = workingReportRepository.findByDateAndUserId(date, user);
    return userWorkingReports.stream()
        .anyMatch(workingReport -> workingReport.getCheckIn() != null);
  }

  /**
   * Formats the date to obtain the ID.
   *
   * @param date The date to format.
   * @return The formatted date ID.
   */
  private String formatDateId(Date date) {
    return new SimpleDateFormat("d").format(date);
  }

  /**
   * Builds a WorkingReportDTO object using the provided information.
   *
   * @param id        The report ID.
   * @param periodId  The period ID.
   * @param userId    The ID of the user.
   * @param date      The date.
   * @param isHoliday Indicates if it's a holiday.
   * @param isCheckin Indicates if the user has a check-in record.
   * @return The built WorkingReportDTO.
   */
  private WorkingReportDTO convertWorkingReportDTO(String id, Long periodId, Long userId, Date date, boolean isHoliday,
      boolean isCheckin) {
    String name = getUserName(userId);
    String roleName = getUserRole(userId);
    String descHoliday = isHoliday ? getHolidayDescription(date) : "";

    ListDateDTO listDateDTO = buildListDateDTO(date, descHoliday, isHoliday, isCheckin);

    return buildWorkingReportDTO(id, periodId, userId, name, roleName, listDateDTO);
  }

  /**
   * Retrieves the description for a holiday date.
   *
   * @param date The holiday date.
   * @return The description of the holiday.
   */
  private String getHolidayDescription(Date date) {
    Holiday holiday = holidayRepository.findByDate(date);
    if (holiday != null) {
      return holiday.getNotes();
    } else {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == Calendar.SATURDAY) {
        return "Saturday";
      } else if (dayOfWeek == Calendar.SUNDAY) {
        return "Sunday";
      } else {
        return "";
      }
    }
  }

  /**
   * Builds a ListDateDTO object using the provided information.
   *
   * @param date        The date.
   * @param descHoliday The holiday description.
   * @param isHoliday   Indicates if it's a holiday.
   * @param isCheckin   Indicates if the user has a check-in record.
   * @return The built ListDateDTO.
   */
  private ListDateDTO buildListDateDTO(Date date, String descHoliday, boolean isHoliday, boolean isCheckin) {
    return ListDateDTO.builder()
        // untuk dto ini belum sesuai
        .date(date)
        .descHoliday(descHoliday)
        .isHoliday(isHoliday)
        .isCheckIn(isCheckin)
        .build();
  }

  /**
   * Builds a WorkingReportDTO object using the provided information.
   *
   * @param id          The report ID.
   * @param periodId    The period ID.
   * @param userId      The ID of the user.
   * @param name        The name of the user.
   * @param roleName    The name of the user's role.
   * @param listDateDTO The list date DTO.
   * @return The built WorkingReportDTO.
   */
  private WorkingReportDTO buildWorkingReportDTO(String id, Long periodId, Long userId, String name, String roleName,
      ListDateDTO listDateDTO) {
    return WorkingReportDTO.builder()
        .id(id)
        .period(periodId)
        .userId(userId)
        .name(name)
        .roleName(roleName)
        .listDate(listDateDTO)
        .build();
  }

  /**
   * 
   * Checks if two dates represent the same day.
   * 
   * @param date1 The first date.
   * @param date2 The second date.
   * @return True if the dates represent the same day, false otherwise.
   */
  private boolean isSameDay(Date date1, Date date2) {
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
        cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * 
   * Retrieves the user with the specified user ID.
   * 
   * @param userId The ID of the user.
   * @return The user object.
   */
  private Users getUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User Data Not Found!"));
  }

  /**
   * 
   * Retrieves the list of holiday dates.
   * 
   * @return The list of holiday dates.
   */
  private List<Date> getHolidayDates() {
    List<Date> holidayDates = new ArrayList<>();
    List<Holiday> holidays = holidayRepository.findAll();
    for (Holiday holiday : holidays) {
      holidayDates.add(holiday.getDate());
    }

    return holidayDates;
  }

  /**
   * 
   * Retrieves the user's full name based on the user ID.
   * 
   * @param userId The ID of the user.
   * @return The user's full name.
   */
  private String getUserName(Long userId) {
    Users user = getUserId(userId);
    return user.getFirstName() + " " + user.getLastName();
  }

  /**
   * 
   * Retrieves the user's role based on the user ID.
   * 
   * @param userId The ID of the user.
   * @return The user's role.
   */
  private String getUserRole(Long userId) {
    Users user = getUserId(userId);
    return user.getPosition().getCodeName();
  }

  public EmployeeAttendanceResponseDTO checkInAttendance(EmployeeAttendanceDTO body) {
    EmployeeAttendanceResponseDTO response = new EmployeeAttendanceResponseDTO();
    try {
      WorkingReport attendance = new WorkingReport();
      attendance.setPeriodId(periodRepository.findById(body.getPeriodId())
          .orElseThrow(() -> new EntityNotFoundException("Period Not Found")));
      attendance.setPresenceId(categoryCodeRepository.findById(body.getPresenceId())
          .orElseThrow(() -> new EntityNotFoundException("Presence Not Found")));
      attendance.setWorkLocation(body.getWorkLocation());
      attendance.setUserId(userRepository.findByUserId(body.getUserId())
          .orElseThrow(() -> new EntityNotFoundException("Employee Not Found")));
      attendance.setDate(body.getDate());
      attendance.setCreatedTime(new Timestamp(System.currentTimeMillis()));
      attendance = workingReportRepository.save(attendance);
      response.setId(attendance.getWorkingReportId());
    } catch (Exception e) {
      log.info(e.getMessage());
      e.getStackTrace();
      throw e;
    }
    return response;
  }

  public EmployeeAttendanceResponseDTO checkInNotAttendance(EmployeeNotAttendanceDTO body) {
    EmployeeAttendanceResponseDTO response = new EmployeeAttendanceResponseDTO();
    try {
      WorkingReport attendance = new WorkingReport();
      attendance.setPeriodId(periodRepository.findById(body.getPeriodId())
          .orElseThrow(() -> new EntityNotFoundException("Period Not Found")));
      attendance.setPresenceId(categoryCodeRepository.findById(body.getPresenceId())
          .orElseThrow(() -> new EntityNotFoundException("Presence Not Found")));
      attendance.setUserId(userRepository.findByUserId(body.getUserId())
          .orElseThrow(() -> new EntityNotFoundException("Employee Not Found")));
      attendance.setDate(body.getDate());
      attendance.setCreatedTime(new Timestamp(System.currentTimeMillis()));
      attendance = workingReportRepository.save(attendance);
      File file = new File();
      file.setWorkingReportId(attendance);
      file.setFileName(body.getFile());
      fileRepository.save(file);
      response.setId(attendance.getWorkingReportId());
    } catch (Exception e) {
      log.info(e.getMessage());
      e.getStackTrace();
      throw e;
    }
    return response;
  }

  public EmployeeAttendanceResponseDTO checkIn(CheckInDTO body) {
    EmployeeAttendanceResponseDTO response = new EmployeeAttendanceResponseDTO();
    try {
      WorkingReport wr = workingReportRepository.findById(body.getWorkingReportId())
          .orElseThrow(() -> new EntityNotFoundException("Working Report Not Found"));
      wr.setCheckIn(body.getCheckInTime());
      wr.setLocationCheckin(body.getLatitude() + ":" + body.getLongitude());
      File fileWr = new File();
      fileWr.setFileName(body.getFile());
      fileWr.setWorkingReportId(wr);
      fileRepository.save(fileWr);
      workingReportRepository.save(wr);
      response.setId(wr.getWorkingReportId());
      return response;
    } catch (Exception e) {
      log.info(e.getMessage());
      e.getStackTrace();
      throw e;
    }
  }

  public @ResponseBody void download(HttpServletResponse response, String reportFormat, Long userId)
          throws JRException, IOException {
    String fileName = generateFileName(reportFormat, userId);
    String contentType = "application/x-" + reportFormat;

    java.io.File file = ResourceUtils.getFile(jrxmlPath);
    JasperDesign design = JRXmlLoader.load(file);
    JasperReport report = JasperCompileManager.compileReport(design);

    List<WorkingReport> listDivision = workingReportRepository.findByUserId(userId);
    JRDataSource dataSource = new JRBeanCollectionDataSource(listDivision);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("datasource", dataSource);

    JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
    response.setContentType(contentType);
    response.setHeader("Content-Disposition", "inline; filename=" + fileName);

    final OutputStream outputStream = response.getOutputStream();

    if (reportFormat.equals("pdf")) {
      JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    } else if (reportFormat.equals("xls")) {
      JRXlsExporter exporterXLS = new JRXlsExporter();
      exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
      exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
      exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
      exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
      exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

      exporterXLS.exportReport();
    }

  }

  private String generateFileName(String fileFormat, Long userId) {
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    String currentDateTime = dateFormatter.format(new Date());
    String fileName = tableName + "_" + userRepository.findByUserId(userId).get().getUserName() + "_" + currentDateTime + "." + fileFormat;

    return fileName;
  }


}
