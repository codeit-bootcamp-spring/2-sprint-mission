package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.repository.JwtSessionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResultMapper {

  private final JwtSessionRepository jwtSessionRepository;

  public UserResult convertToUserResult(User user) {
    return UserResult.fromEntity(user, isOnline(user));
  }

  private boolean isOnline(User user) {
    Optional<JwtSession> session = jwtSessionRepository.findById(user.getId());
    return session.isPresent();
  }

}
