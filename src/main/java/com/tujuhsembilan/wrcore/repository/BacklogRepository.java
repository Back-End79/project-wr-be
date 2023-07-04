package com.tujuhsembilan.wrcore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tujuhsembilan.wrcore.model.Backlog;

public interface BacklogRepository
    extends PagingAndSortingRepository<Backlog, Long>, JpaSpecificationExecutor<Backlog> {

  Optional<Backlog> findByBacklogId(Long backlogId);

}
