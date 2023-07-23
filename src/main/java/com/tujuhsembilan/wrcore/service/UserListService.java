package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.ol.dto.UserListDTO;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserListService {
    private final UserRepository userRepository;

    public List<UserListDTO> getUserList(String categoryName, String search) {
        return userRepository.getUserList(categoryName, search);
    }
}
