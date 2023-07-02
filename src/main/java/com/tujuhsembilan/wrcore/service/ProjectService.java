package com.tujuhsembilan.wrcore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.ol.dto.OLProjectDTO;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

  private final ProjectRepository projectRepository;

  public List<OLProjectDTO> findAllByProjectNameAndSearch(String search) {
    return projectRepository.findAllByProjectNameAndSearch(search.toLowerCase());
  }

}
