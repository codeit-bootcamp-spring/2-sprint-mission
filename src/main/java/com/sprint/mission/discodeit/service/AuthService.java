package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public User login(LoginRequest loginRequest) {
    User user = userRepository.findAll().stream()
        .filter(u -> u.getUsername().equals(loginRequest.username()))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("Invalid newUsername"));

    if (!user.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("Invalid newPassword");
    }

    return user;
  }
}