package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public void login(LoginRequest loginRequest) {
    User userInfo = userRepository.findUserByUsername(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException(
            String.format("User with username %s not found", loginRequest.username())));

    if (!userInfo.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("Wrong password");
    }
  }
}
