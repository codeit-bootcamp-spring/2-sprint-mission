package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@RequiredArgsConstructor
@Component
public class AccessRoles {

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final ReadStatusRepository readStatusRepository;

  public AuthorizationManager<RequestAuthorizationContext> userOrAdmin() {
    return (authentication, context) -> {
      String userIdFromPath = extractPathVariable(context, "/api/users/{userId}", "userId");
      String currentUserId = authentication.get().getName();
      boolean isAdmin = authentication.get().getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

      return new AuthorizationDecision(currentUserId.equals(userIdFromPath) || isAdmin);
    };
  }

  public AuthorizationManager<RequestAuthorizationContext> messageOwner() {
    return (authentication, context) -> {
      Optional<UUID> messageId = Optional.ofNullable(
          extractPathVariable(context, "/api/messages/{messageId}", "messageId")
      ).map(UUID::fromString);

      Optional<UUID> currentUserId = Optional.ofNullable(
          authentication.get().getName()
      ).map(UUID::fromString);

      // 예: 메시지 소유자 검증 (DB 조회 필요할 수 있음)
      boolean isOwner = readStatusRepository.existsByUserIdAndChannelId(currentUserId.orElseThrow(),
          messageId.orElseThrow());

      return new AuthorizationDecision(isOwner);
    };
  }

  public AuthorizationManager<RequestAuthorizationContext> messageOwnerOrAdmin() {
    return (authentication, context) -> {
      Optional<UUID> messageId = Optional.ofNullable(
          extractPathVariable(context, "/api/messages/{messageId}", "messageId")
      ).map(UUID::fromString);

      Optional<UUID> currentUserId = Optional.ofNullable(
          authentication.get().getName()
      ).map(UUID::fromString);

      boolean isOwner = readStatusRepository.existsByUserIdAndChannelId(messageId.orElseThrow(),
          currentUserId.orElseThrow());
      boolean isAdmin = authentication.get().getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

      return new AuthorizationDecision(isOwner || isAdmin);
    };
  }

  public AuthorizationManager<RequestAuthorizationContext> userOnly() {
    return (authentication, context) -> {
      String currentUserId = authentication.get().getName();
      String targetUserId = extractPathVariable(context, "/api/messages/{messageId}/read-status",
          "userId");
      return new AuthorizationDecision(currentUserId.equals(targetUserId));
    };
  }

  public static String extractPathVariable(RequestAuthorizationContext context, String uriPattern,
      String variableName) {
    HttpServletRequest request = context.getRequest();
    String uri = request.getRequestURI();

    if (!pathMatcher.match(uriPattern, uri)) {
      return null;
    }

    Map<String, String> variables = pathMatcher.extractUriTemplateVariables(uriPattern, uri);
    return variables.get(variableName);
  }
}
