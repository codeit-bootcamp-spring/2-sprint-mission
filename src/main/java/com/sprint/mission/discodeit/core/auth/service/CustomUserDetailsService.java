package com.sprint.mission.discodeit.core.auth.service;

import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final JpaUserRepository userRepository;
  private final SessionRegistry sessionRegistry;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByName(username).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND)
    );

    return new CustomUserDetails(UserDto.from(user), user.getPassword());
  }

  public UserDto updateRole(UserRoleUpdateRequest request) {
    UUID id = request.userId();
    User user = userRepository.findById(id).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND, id));
    user.updateRole(request.newRole());
    userRepository.save(user);
    expireUserSession(user.getId());

    return UserDto.from(user);
  }

  private void expireUserSession(UUID id) {
    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

    for (Object principal : allPrincipals) {
      if (principal instanceof CustomUserDetails
          && ((CustomUserDetails) principal).getUserDto().id().equals(id)) {
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
        for (SessionInformation session : allSessions) {
          session.expireNow();
        }
      }
    }
  }
}
