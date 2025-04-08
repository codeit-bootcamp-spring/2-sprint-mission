package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BasicAuthServiceImp implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new NoSuchElementException("User with username " + username + " not found"));

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("Wrong password");
    }

    return user;
  }
}
