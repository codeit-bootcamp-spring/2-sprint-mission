package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.read.repository.JpaReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//TODO 어떻게 수정해야할 지 몰라서 일단 냅둡니다.
@Component("customSecurity")
@RequiredArgsConstructor
public class CustomSecurity {

  private final JpaMessageRepository messageRepository;
  private final JpaReadStatusRepository readStatusRepository;

  public boolean isMessageOwner(UUID messageId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return messageRepository.findById(messageId)
        .map(message -> message.getAuthor().getId().equals(currentUserId)).orElse(false);
  }

  public boolean isReadStatusOwner(UUID readStatusId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(
        authentication.getPrincipal())) {
      return false;
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return readStatusRepository.findById(readStatusId)
        .map(readStatus -> readStatus.getUser().getId().equals(currentUserId))
        .orElse(false);
  }
}
