package com.tujuhsembilan.wrcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.Backlog;
import java.util.Optional;

public interface BacklogRepository extends JpaRepository<Backlog, Long> {

  Optional<Backlog> findByBacklogId(Long backlogId);

}
