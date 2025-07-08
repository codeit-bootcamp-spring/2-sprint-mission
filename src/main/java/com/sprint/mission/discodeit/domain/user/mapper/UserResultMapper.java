package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResultMapper {

  private final SessionRegistry sessionRegistry;

  public UserResult convertToUserResult(User user) {
    Set<UUID> onlineUserIds = sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> !sessionRegistry.getAllSessions(principal, false).isEmpty())
        .filter(principal -> principal instanceof CustomUserDetails)
        .map(principal -> ((CustomUserDetails) principal).getUserResult().id())
        .collect(Collectors.toSet());

    return UserResult.fromEntity(user, onlineUserIds.contains(user.getId()));
  }

}
