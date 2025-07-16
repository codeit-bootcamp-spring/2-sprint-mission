package com.sprint.mission.discodeit.security.jwt.accessmanager;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageDeleteAuthorizationManager implements
    AuthorizationManager<RequestAuthorizationContext> {

  private final MessageRepository messageRepository;

  @Transactional(readOnly = true)
  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> auth,
      RequestAuthorizationContext context
  ) {
    String messageId = context.getVariables().get("messageId");
    Authentication authentication = auth.get();
    UUID currentUserId = ((UserResult) authentication.getPrincipal()).id();

    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    Optional<Message> message = messageRepository.findByMessageId(UUID.fromString(messageId));
    if (message.isPresent()) {
      boolean isOwner = currentUserId.equals(message.get().getUser().getId());
      return new AuthorizationDecision(isOwner || isAdmin);
    }

    return new AuthorizationDecision(false);
  }

}