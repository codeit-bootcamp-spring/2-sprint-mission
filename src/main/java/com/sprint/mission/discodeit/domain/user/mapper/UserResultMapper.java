package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResultMapper {

  private final SessionRegistry sessionRegistry;

  public UserResult convertToUserResult(User user) {
    List<SessionInformation> allSessions = sessionRegistry.getAllSessions(user.getName(),
        false);

    if (allSessions.isEmpty()) {
      return UserResult.fromEntity(user, false);
    }

    return UserResult.fromEntity(user, true);
  }

}
