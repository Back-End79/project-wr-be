package com.tujuhsembilan.wrcore.repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.UserRole;
import com.tujuhsembilan.wrcore.model.Users;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {   
    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :id AND ur.isActive = true AND ur.roleId IS NOT NULL")
    List<UserRole> findByIdAndActiveList (Users id);
    
    @Query(nativeQuery = true, value = "SELECT ur.user_id, first_name AS name , u.nip AS nip FROM user_role ur " +
        "LEFT JOIN users u ON ur.user_id = u.user_id " +
        "LEFT JOIN category_code cc ON ur.role_id = cc.category_code_id " +
        "WHERE (ur.is_active = true AND (:search IS NULL OR LOWER(u.first_name) LIKE %:search% OR LOWER(u.nip) LIKE %:search% )) " +
        "GROUP BY ur.user_id, u.first_name, u.nip, u.last_name")
    List<Map<String, Object>> findAllUserRoleId(Pageable pageable, @Param("search") String search);
    
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :id AND ur.isActive = true")
    List<UserRole> findByUserIdList(Users id);

    @Query("SELECT ur FROM UserRole ur WHERE ur.roleId = :id")
    List<UserRole> findByCategoryIdList(CategoryCode id);

    @Query("SELECT ur FROM UserRole ur WHERE ur.roleId = :id AND ur.userId = :uid")
    Optional<UserRole> findByUserRoleIdAndUserId(CategoryCode id, Users uid);

    @Query("SELECT ur FROM UserRole ur WHERE ur.userId =:searchId")
    UserRole findUserRoleByUserId(@Param("searchId")Users searchId);
}
