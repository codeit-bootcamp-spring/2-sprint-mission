package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.PasswordMismatchException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
    String username = request.username();
    String password = request.password();

    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new UserNotFoundException("User with name " + username + " not found"));

    if (!user.getPassword().equals(password)) {
      throw new PasswordMismatchException("Password mismatch");
    }

    return user;
  }
}
