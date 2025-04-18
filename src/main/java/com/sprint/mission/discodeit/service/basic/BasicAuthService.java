package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto2.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto2.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public User login(UserLoginRequest loginRequest) {
    String username = loginRequest.username();
//    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    return user;
  }
}