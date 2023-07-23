package com.tujuhsembilan.wrcore.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.model.UserUtilization;
import com.tujuhsembilan.wrcore.model.Users;


public interface UserUtilizationRepository extends JpaRepository<UserUtilization, Long> {
    @Query("SELECT uu FROM UserUtilization uu WHERE uu.projectId = :id AND uu.isActive = true")
    List<UserUtilization> findByProjectIdList(Project id);

    @Query("SELECT uu FROM UserUtilization uu JOIN Users u ON uu.userId = u WHERE uu.projectId = :id AND uu.isActive = true")
    Page<UserUtilization> findByProjectIdPage(Project id, Pageable pageable);
    
    List<UserUtilization> findByProjectIdEquals(Long id);

    @Query("SELECT uu FROM UserUtilization uu WHERE uu.projectId = :id AND uu.userId = :uid")
    Optional<UserUtilization> findByProjectIdAndUserId(Project id, Users uid);

    @Query("SELECT uu FROM UserUtilization uu WHERE uu.projectId = :id AND uu.userId = :uid AND uu.isActive = true")
    List<UserUtilization> findByProjectIdAndUserIdList(Project id, Users uid);
    
}
