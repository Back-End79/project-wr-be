package com.tujuhsembilan.wrcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.tujuhsembilan.wrcore.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

  Optional<Users> findByUserId(Long id);
  Optional<Users> findByUserName(@Param("user_name") String userName);
	Optional<Users> findByNip(@Param("nip") String nip); 
	// 
  List<Users> findByUserNameContainingIgnoreCase(String userName);   

}
