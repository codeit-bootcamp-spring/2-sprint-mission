package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.LoginRequest;
import com.sprint.mission.discodeit.entity._User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public _User login(LoginRequest loginRequest) {
    String username = loginRequest.getUsername().toString();
    String password = loginRequest.getPassword().toString();

    _User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new NoSuchElementException("User with username " + username + " not found"));

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("Wrong password");
    }

    return user;
  }
}
