package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
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
  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findUserByUsername(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException(
            String.format("User with username %s not found", loginRequest.username())));

    if (!user.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("Wrong password");
    }
    return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getCreatedAt(), user.getUpdatedAt(), user.getProfileId());
  }
}
