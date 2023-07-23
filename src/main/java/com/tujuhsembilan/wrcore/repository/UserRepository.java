package com.tujuhsembilan.wrcore.repository;

import java.util.List;
import java.util.Optional;

import com.tujuhsembilan.wrcore.ol.dto.UserListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.ol.dto.TeamMemberDTO;
import com.tujuhsembilan.wrcore.ol.dto.UserOLDTO;

public interface UserRepository extends JpaRepository<Users, Long> {

  Optional<Users> findByUserId(Long id);
      
  Optional<Users> findByUserName(@Param("user_name") String userName);

  Optional<Users> findByNip(@Param("nip") String nip);
  @Query("SELECT u " +
  "FROM Users u " + 
  "WHERE (:search IS NULL OR LOWER(u.firstName) LIKE %:search% " +
  "OR LOWER(u.nip) LIKE %:search% " +
  "OR LOWER(u.lastContractStatus) LIKE %:search%)")
Page<Users> findUser(@Param("search") String search, Pageable pageable);   

  @Query(value = "SELECT COUNT(*) FROM Users")
  int countUser();

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.UserOLDTO(uo) FROM Users uo " +
         "WHERE (:search IS NULL OR LOWER(uo.firstName) LIKE %:search% OR LOWER(uo.lastName)" +
         " LIKE %:search% OR LOWER(uo.nip) LIKE %:search%) AND uo.isActive = true")
  List<UserOLDTO> getOlUser(String search);

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.TeamMemberDTO(tm) FROM Users tm " +
         "JOIN tm.position CategoryCode " +
         "WHERE (:search IS NULL OR LOWER(tm.firstName) LIKE %:search% " +
                                "OR LOWER(tm.lastName) LIKE %:search% " +
                                "OR LOWER(tm.nip) LIKE %:search% " + 
                                "OR LOWER(CategoryCode.codeName) LIKE %:search%) " +
         "AND tm.isActive = true AND tm.group = 8")
  Page<TeamMemberDTO> findByIsActiveAndTalent(String search, Pageable pageable);

  @Query("SELECT u FROM Users u WHERE u.userId = :id AND u.isActive = true")
  Optional<Users> findByIdAndActive(Long id);

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.UserListDTO(us) FROM Users us " +
          "LEFT JOIN CategoryCode cc ON us.group.categoryCodeId = cc.categoryCodeId " +
          "WHERE (:search IS NULL OR LOWER(CONCAT(us.firstName, ' ', us.lastName) ) LIKE %:search% " +
          "OR LOWER(cc.codeName) LIKE %:search%) AND (cc.codeName) LIKE %:codeName% " +
          "AND us.isActive = true ")
  List<UserListDTO> getUserList(String codeName, String search);
}
