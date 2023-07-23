package com.tujuhsembilan.wrcore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.ListTaskDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeDetailDTO;
import com.tujuhsembilan.wrcore.dto.OvertimeResponseDTO;
import com.tujuhsembilan.wrcore.model.Backlog;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.NotificationEmail;
import com.tujuhsembilan.wrcore.model.Period;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.model.Task;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.repository.BacklogRepository;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.PeriodRepository;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import com.tujuhsembilan.wrcore.repository.TaskRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OvertimeService {

  private final TaskRepository taskRepository;
  private final WorkingReportRepository workingReportRepository;
  private final BacklogRepository backlogRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final UserRepository userRepository;
  private final PeriodRepository periodRepository;
  private final ProjectRepository projectRepository;
  private final MailService mailService;

  @Transactional
  public OvertimeResponseDTO createOvertime(OvertimeDTO overtimeDTO) {
    try {
      LocalDate currentDate = LocalDate.now();
      overtimeDTO.setWorkDate(currentDate);

      Backlog backlog = getBacklogById(overtimeDTO.getBacklogId());
      CategoryCode statusBacklog = getCategoryCodeById(overtimeDTO.getStatusTask());

      checkExistingOvertime(backlog, overtimeDTO.getWorkDate());
      updateBacklog(backlog, statusBacklog, overtimeDTO.getDuration());
      WorkingReport savedWorkingReport = saveWorkingReport(overtimeDTO);
      Task savedTask = saveOvertimeTask(savedWorkingReport, backlog, overtimeDTO);

      sendOvertimeNotification(savedTask, savedWorkingReport);
      return convertToDTO(savedTask, savedWorkingReport);

    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  public OvertimeDetailDTO getOvertimeDetail(Long workingReportId) {
    WorkingReport workingReport = getWorkingReportById(workingReportId);
    Task task = getTaskByWorkingReport(workingReport);
    Project project = getProjectByTask(task);
    String projectName = generateProjectName(task);
    List<ListTaskDTO> listTaskDTOs = getListTaskDTOsByProject(project);

    return convertToDetailDTO(task, projectName, listTaskDTOs);
  }

  /* --- End of main function Overtime --- */

  /* Several functions are used to construct the overtime create function. */

  private void checkExistingOvertime(Backlog backlog, LocalDate workDate) {
    Task existingTask = taskRepository.findByBacklogIdAndWorkDate(backlog, workDate);
    if (existingTask != null && existingTask.isOvertime()) {
      throw new EntityExistsException("Overtime for the same backlogId and workDate already exists");
    }
  }

  private WorkingReport saveWorkingReport(OvertimeDTO overtimeDTO) {
    Long periodId = getMonthNumberFromWorkDate(overtimeDTO);
    Period period = getPeriodById(periodId);
    CategoryCode presenceId = getCategoryCodeById(42L);
    Users user = getUserByUserId(overtimeDTO.getCreatedBy());
    WorkingReport workingReport = createWorkingReportMapper(user, period, presenceId, overtimeDTO);
    return workingReportRepository.save(workingReport);
  }

  private Task saveOvertimeTask(WorkingReport savedWorkingReport, Backlog backlog, OvertimeDTO overtimeDTO) {
    Task overtimeTask = createOvertimeMapper(savedWorkingReport, backlog, overtimeDTO);
    return taskRepository.save(overtimeTask);
  }

  private void updateBacklog(Backlog backlog, CategoryCode statusBacklog, BigDecimal actualTime) {
    backlog.setStatusBacklog(statusBacklog);
    backlog.setActualTime(addBigDecimal(backlog.getActualTime(), actualTime));
    backlogRepository.save(backlog);
  }

  /* Several functions are used to construct the overtime detail function. */

  private Project getProjectByTask(Task task) {
    Backlog backlog = task.getBacklogId();
    Long projectId = backlog.getProjectId().getProjectId();
    return getProjectById(projectId);
  }

  private List<ListTaskDTO> getListTaskDTOsByProject(Project project) {
    List<Backlog> backlogs = backlogRepository.findByProjectId(project);
    return backlogs.stream()
        .flatMap(backlog -> taskRepository.findByBacklogId(backlog).stream())
        .map(this::convertToListTaskDTO)
        .collect(Collectors.toList());
  }

  private String generateProjectName(Task task) {
    Backlog backlog = task.getBacklogId();
    return backlog.getTaskCode() + " - " + backlog.getProjectId().getProjectName();
  }

  /* Some functions are used to create mappers and convert entities to DTOs */

  private Task createOvertimeMapper(WorkingReport workingReport, Backlog backlog, OvertimeDTO overtimeDTO) {
    return Task.builder()
        .backlogId(backlog)
        .workingReportId(workingReport)
        .taskItem(overtimeDTO.getTaskItem())
        .duration(overtimeDTO.getDuration())
        .isOvertime(true)
        .createdOn(new Timestamp(System.currentTimeMillis()))
        .createdBy(overtimeDTO.getCreatedBy())
        .updatedOn(new Timestamp(System.currentTimeMillis()))
        .updatedBy(overtimeDTO.getCreatedBy())
        .workDate(overtimeDTO.getWorkDate())
        .build();
  }

  private WorkingReport createWorkingReportMapper(Users user, Period period, CategoryCode presenceId,
      OvertimeDTO overtimeDTO) {
    return WorkingReport.builder()
        .periodId(period)
        .presenceId(presenceId)
        .userId(user)
        .date(java.sql.Date.valueOf(overtimeDTO.getWorkDate()))
        .totalHours(overtimeDTO.getDuration())
        .createdTime(new Timestamp(System.currentTimeMillis()))
        .lastModifiedTime(new Timestamp(System.currentTimeMillis()))
        .isHoliday(false) // Assuming this should be set based on business logic
        .isOvertime(true)
        .startTime(LocalTime.parse(overtimeDTO.getStartTime(), DateTimeFormatter.ofPattern("HH:mm")))
        .endTime(LocalTime.parse(overtimeDTO.getEndTime(), DateTimeFormatter.ofPattern("HH:mm")))
        .build();
  }

  public OvertimeResponseDTO convertToDTO(Task overtimeTask, WorkingReport workingReport) {
    return OvertimeResponseDTO.builder()
        .workingReportId(workingReport.getWorkingReportId())
        .taskId(overtimeTask.getTaskId())
        .build();
  }

  private OvertimeDetailDTO convertToDetailDTO(Task task, String projectName, List<ListTaskDTO> listTaskDTOs) {
    return OvertimeDetailDTO.builder()
        .workingReportId(task.getWorkingReportId().getWorkingReportId())
        .projectId(task.getBacklogId().getProjectId().getProjectId())
        .projectName(projectName)
        .listTask(listTaskDTOs)
        .build();
  }

  private ListTaskDTO convertToListTaskDTO(Task task) {
    return ListTaskDTO.builder()
        .taskId(task.getTaskId())
        .backlogId(task.getBacklogId().getBacklogId())
        .taskCode(task.getBacklogId().getTaskCode())
        .taskName(task.getBacklogId().getTaskName())
        .statusTaskId(task.getBacklogId().getStatusBacklog().getCategoryCodeId())
        .statusTaskName(task.getBacklogId().getStatusBacklog().getCodeName())
        .taskDescription(task.getBacklogId().getTaskDescription())
        .taskItem(task.getTaskItem())
        .duration(task.getDuration())
        .createdOn(task.getCreatedOn())
        .createBy(task.getCreatedBy())
        .updateOn(task.getUpdatedOn())
        .updateBy(task.getUpdatedBy())
        .build();
  }

  /* Functions used for sending notification emails */

  private void sendOvertimeNotification(Task savedTask, WorkingReport savedWorkingReport) {
    NotificationEmail mailMessage = createNotificationEmail(savedTask, savedWorkingReport);
    mailService.sendMail(mailMessage);
  }

  private NotificationEmail createNotificationEmail(Task overtimeTask, WorkingReport workingReport) {
    String fullName = workingReport.getUserId().getFirstName() + " " +
        workingReport.getUserId().getLastName();
    String formattedDate = formatDate(workingReport.getDate());
    String period = new SimpleDateFormat("MMMM").format(workingReport.getDate());

    String recipientEmail = workingReport.getUserId().getEmail();
    String subject = "Notification: Overtime " + fullName + " - " + period;

    NotificationEmail notificationEmail = new NotificationEmail();
    notificationEmail.setRecipient(recipientEmail);
    notificationEmail.setSubject(subject);
    notificationEmail.setFormattedDate(formattedDate);
    notificationEmail.setFullName(fullName);
    notificationEmail.setProjectName(generateProjectName(overtimeTask));
    notificationEmail.setTaskDescription(overtimeTask.getTaskItem());
    notificationEmail.setActualEffort(overtimeTask.getDuration().toString());

    return notificationEmail;
  }

  /* Utility functions to fulfill the needs of other functions requiring them */

  public BigDecimal addBigDecimal(BigDecimal value1, BigDecimal value2) {
    if (value1 != null && value2 != null) {
      return value1.add(value2);
    } else if (value1 != null) {
      return value1;
    } else if (value2 != null) {
      return value2;
    } else {
      return BigDecimal.ZERO; // or null if appropriate for your use case
    }
  }

  public Long getMonthNumberFromWorkDate(OvertimeDTO overtimeDTO) {
    LocalDate workDate = overtimeDTO.getWorkDate();
    if (workDate != null) {
      return (long) workDate.getMonthValue();
    } else {
      return null; // Return null or throw an exception if needed, for an invalid or null workDate
    }
  }

  public String formatDate(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
    return dateFormat.format(date);
  }

  /* Utility methods to fetch entities by their unique IDs or associations */

  private Backlog getBacklogById(Long backlogId) {
    return backlogRepository.findById(backlogId)
        .orElseThrow(() -> new EntityNotFoundException("Backlog ID Not Found!"));
  }

  private CategoryCode getCategoryCodeById(Long categoryCodeId) {
    return categoryCodeRepository.findById(categoryCodeId)
        .orElseThrow(() -> new EntityNotFoundException("Category Code ID Not Found!"));
  }

  private Users getUserByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User ID Not Found!"));
  }

  private Period getPeriodById(Long periodId) {
    return periodRepository.findById(periodId)
        .orElseThrow(() -> new EntityNotFoundException("Period ID Not Found!"));
  }

  private WorkingReport getWorkingReportById(Long workingReportId) {
    return workingReportRepository.findById(workingReportId)
        .orElseThrow(() -> new EntityNotFoundException("working Report ID Not Found!"));
  }

  private Project getProjectById(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project ID Not Found!"));
  }

  private Task getTaskByWorkingReport(WorkingReport workingReport) {
    return taskRepository.findByWorkingReportId(workingReport)
        .orElseThrow(() -> new EntityNotFoundException("Task based on Working Report ID Not Found!"));
  }

}
