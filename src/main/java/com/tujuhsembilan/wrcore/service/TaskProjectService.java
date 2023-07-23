package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.ol.dto.TaskProjectDTO;
import com.tujuhsembilan.wrcore.repository.BacklogRepository;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskProjectService {

    private final BacklogRepository backlogRepository;
    private final ProjectRepository projectRepository;

    public List<TaskProjectDTO> getAllTaskProject(Long projectId, String search) {
        return backlogRepository.findTaskProjectAll(projectRepository.findById(projectId).orElseThrow(), search);
    }
}
