package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.ol.dto.ProjectTypeListDTO;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectTypeListService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<ProjectTypeListDTO> getListProjectOfUser(Long userId, String search) {
        return projectRepository.findAllProjectType(userRepository.findById(userId).orElseThrow(), search);
    }
}
