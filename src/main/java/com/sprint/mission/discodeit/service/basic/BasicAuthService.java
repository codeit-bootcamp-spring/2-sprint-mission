package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public User login(LoginRequest request) {
    User matchedUser = userRepository.findAll().stream()
        .filter(user -> user.getUsername().equals(request.username()))
        .filter((user -> user.getPassword().equals(request.password())))
        .findFirst()
        .orElse(null);

    if (matchedUser == null) {
      throw new IllegalArgumentException("[Error] matchedUser is null");
    }

    return matchedUser;
  }


}
