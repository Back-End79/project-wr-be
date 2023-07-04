package com.tujuhsembilan.wrcore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.TaskDTO;
import com.tujuhsembilan.wrcore.model.Task;
import com.tujuhsembilan.wrcore.repository.BacklogRepository;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.TaskRepository;
import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;

import lombok.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {

  private final TaskRepository taskRepository;
  private final ModelMapper modelMapper;
  private final BacklogRepository backlogRepository;
  private final WorkingReportRepository workingReportRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public Task createTask(TaskDTO taskDTO) throws NotFoundException {
    Task task = Task.builder()
        .backlogId(backlogRepository.findById(taskDTO.getBacklogId()).orElseThrow(() -> new NotFoundException()))
        .workingReportId(
            workingReportRepository.findById(taskDTO.getWorkingReportId()).orElseThrow(() -> new NotFoundException()))
        .taskItem(taskDTO.getTaskItem())
        .duration(taskDTO.getDuration())
        .isOvertime(false)
        .build();
    return taskRepository.save(task);
  }
}
