// package com.tujuhsembilan.wrcore.service;

// import com.tujuhsembilan.wrcore.dto.AddTaskDTO;
// import com.tujuhsembilan.wrcore.dto.listTaskItemDTO;
// import com.tujuhsembilan.wrcore.model.Backlog;
// import com.tujuhsembilan.wrcore.model.Task;
// import com.tujuhsembilan.wrcore.model.WorkingReport;
// import com.tujuhsembilan.wrcore.repository.BacklogRepository;
// import com.tujuhsembilan.wrcore.repository.TaskRepository;
// import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import javax.persistence.EntityExistsException;
// import javax.persistence.EntityNotFoundException;
// import java.sql.Timestamp;
// import java.util.Optional;
// import java.sql.Date;

// @Slf4j
// @Service
// @RequiredArgsConstructor(onConstructor = @__(@Autowired))
// public class TaskService {

// Date today = new Date(System.currentTimeMillis());
// private final TaskRepository taskRepository;
// private final BacklogRepository backlogRepository;
// private final WorkingReportRepository workingReportRepository;

// @Transactional
// public Task createTask(AddTaskDTO taskDTO) {
// try {
// Task task = new Task();
// Backlog idBacklog =
// backlogRepository.findById(taskDTO.getBacklogId()).orElseThrow(() -> new
// EntityNotFoundException("Backlog Id Not Found"));
// WorkingReport idWorking =
// workingReportRepository.findById(taskDTO.getWorkingReportId()).orElseThrow(()
// -> new EntityNotFoundException("Working Report Id Not Found"));
// Optional<Task> cek = taskRepository.findBacklogIdAndWorkDate(idBacklog,
// today);
// for (listTaskItemDTO idTask : taskDTO.getListTask()) {
// if(!cek.isPresent()){
// task = Task.builder()
// .backlogId(idBacklog)
// .workingReportId(idWorking)
// .taskItem(idBacklog.getTaskName())
// .duration(idTask.getDuration())
// .isOvertime(false)
// .createdBy(idTask.getCreatedBy())
// .createdOn(new Timestamp(System.currentTimeMillis()))
// .updatedBy(idTask.getUpdatedBy())
// .updatedOn(new Timestamp(System.currentTimeMillis()))
// .workDate(today)
// .build();
// taskRepository.save(task);
// } else {
// throw new EntityExistsException("Backlog And Work Date Already Exist!");
// }
// }
// return task;
// } catch (Exception ex) {
// log.info(ex.getMessage());
// ex.printStackTrace();
// throw ex;
// }
// }

// public listTaskItemDTO listItemTask(Task task) {
// return listTaskItemDTO.builder()
// .backlogId(task.getBacklogId().getBacklogId())
// .workingReportId(task.getWorkingReportId().getWorkingReportId())
// .taskItem(task.getBacklogId().getTaskName())
// .taskDetail(task.getBacklogId().getTaskDescription())
// .duration(task.getDuration())
// .isOvertime(false)
// .createdBy(task.getCreatedBy())
// .createdOn(new Timestamp(System.currentTimeMillis()))
// .updatedBy(task.getUpdatedBy())
// .updateOn(new Timestamp(System.currentTimeMillis()))
// .workDate(today)
// .build();
// }

// }
