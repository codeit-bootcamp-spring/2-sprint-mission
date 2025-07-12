package com.sprint.mission.discodeit.core.auth.service;

import com.sprint.mission.discodeit.core.auth.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final JpaUserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;

  @Override
  @Cacheable("users")
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND)
    );

    return new CustomUserDetails(UserDto.from(user), user.getPassword());
  }

  @Transactional
  public UserDto updateRole(UserRoleUpdateRequest request) {
    UUID id = request.userId();
    User user = userRepository.findByUserId(id).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND, id));
    user.updateRole(request.newRole());
    userRepository.save(user);

    jwtSessionRepository.findByUserId(id).ifPresent(jwtSessionRepository::delete);

    return UserDto.from(user);
  }
}
