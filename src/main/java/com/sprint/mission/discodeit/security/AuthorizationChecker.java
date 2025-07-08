package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationChecker {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  public boolean isSameUser(UUID userId, Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
      return false;
    }
    return userId.equals(userDetails.getUserId());
  }

  public boolean isMessageOwner(UUID messageId, Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
      return false;
    }

    UUID currentUserId = userDetails.getUserId();

    return messageRepository.findAuthorIdById(messageId)
        .map(authorId -> authorId.equals(currentUserId))
        .orElse(false);
  }

  public boolean isReadStatusOwner(UUID readStatusId, Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
      return false;
    }

    UUID currentUserId = userDetails.getUserId();

    return readStatusRepository.findUserIdById(readStatusId)
        .map(userId -> userId.equals(currentUserId))
        .orElse(false);
  }

}
