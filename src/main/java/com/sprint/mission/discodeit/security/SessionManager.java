package com.sprint.mission.discodeit.security;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionManager {

  private final SessionRegistry sessionRegistry;

  public void expireUserSessions(UUID userId) {
    for (Object principal : sessionRegistry.getAllPrincipals()) {
      if (principal instanceof CustomUserDetails userDetails &&
          userDetails.getUserDto().id().equals(userId)) {

        for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
          session.expireNow();
        }
      }
    }
  }


  public List<UUID> getOnlineUserIds() {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof CustomUserDetails)
        .map(principal -> ((CustomUserDetails) principal).getUserDto().id())
        .toList();
  }


}
