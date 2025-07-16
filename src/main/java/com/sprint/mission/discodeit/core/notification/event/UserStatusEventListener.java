package com.sprint.mission.discodeit.core.notification.event;

import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.user.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusEventListener {

  private final UserStatusService userStatusService;

  @EventListener
  public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
    Object principal = event.getAuthentication().getPrincipal();
    if (principal instanceof CustomUserDetails) {
      UUID userId = ((CustomUserDetails) principal).getUserDto().id();
      userStatusService.setUserOnline(userId);
    }
  }

  @EventListener
  public void handleSessionDestroyed(HttpSessionDestroyedEvent event) {
    SecurityContext securityContext = (SecurityContext) event.getSession()
        .getAttribute("SPRING_SECURITY_CONTEXT");

    if (securityContext != null) {
      Authentication authentication = securityContext.getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userStatusService.setUserOffline(userDetails.getUserDto().id());
      }
    }
  }
}
