package com.sprint.mission.discodeit.security.jwt.accessmanager;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserSelfAuthorizationManager implements
    AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authSupplier,
      RequestAuthorizationContext context
  ) {
    Authentication auth = authSupplier.get();
    String userId = ((UserResult) auth.getPrincipal()).id().toString();

    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    String pathUserId = context.getVariables().get("userId");

    boolean isOwner = userId.equals(pathUserId);
    return new AuthorizationDecision(isOwner || isAdmin);
  }

}