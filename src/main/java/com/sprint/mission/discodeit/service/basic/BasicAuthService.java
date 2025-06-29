package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  @Override
  @Transactional
  public UserDto updateRole(UserRoleUpdateRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

    user.setRole(request.role());
    userRepository.save(user);

    sessionRegistry.getAllPrincipals().forEach(principal -> {
      if (principal instanceof CustomUserDetails details) {
        if (details.getUsername().equals(request.username())) {
          sessionRegistry.getAllSessions(principal, false)
              .forEach(SessionInformation::expireNow);
        }
      }
    });

    return userMapper.toDto(user);
  }
}
