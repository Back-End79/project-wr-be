package com.tujuhsembilan.wrcore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Backlog;
import java.util.Optional;

public interface BacklogRepository extends JpaRepository<Backlog, Long> {

  Page<Backlog> findByTaskNameContainingIgnoreCase(String taskName, Pageable pageable);

  Optional<Backlog> findByBacklogId(Long backlogId);

}
