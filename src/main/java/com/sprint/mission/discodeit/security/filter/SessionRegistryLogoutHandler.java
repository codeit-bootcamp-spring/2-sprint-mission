package com.sprint.mission.discodeit.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    HttpSession session = request.getSession();
    if (session != null) {
//      SessionInformation sessionInformation = sessionRegistry.getSessionInformation(
//          session.getId());
//
//      if (sessionInformation != null) {
//        sessionInformation.expireNow();
//      }

      session.invalidate();
    }
  }

}
