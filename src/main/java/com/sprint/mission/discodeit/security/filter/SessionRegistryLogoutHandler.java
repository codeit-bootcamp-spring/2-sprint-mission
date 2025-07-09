package com.sprint.mission.discodeit.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class SessionRegistryLogoutHandler implements LogoutHandler {

  private final SessionRegistry sessionRegistry;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication
  ) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<SessionInformation> allSessions = sessionRegistry.getAllSessions(auth.getPrincipal(),
        false);
    for (SessionInformation sessionInformation : allSessions) {
      sessionInformation.expireNow();
    }
  }

}
