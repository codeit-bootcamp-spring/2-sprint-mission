package com.sprint.mission.discodeit.security.jwt.accessmanager;

import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
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
public class ReadStatusSelfAuthorizationManager implements
    AuthorizationManager<RequestAuthorizationContext> {

  private final ReadStatusRepository readStatusRepository;

  @Transactional(readOnly = true)
  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> auth,
      RequestAuthorizationContext context
  ) {
    String readStatusId = context.getVariables().get("readStatusId");
    UUID currentUserId = ((UserResult) auth.get().getPrincipal()).id();

    Optional<ReadStatus> readStatus = readStatusRepository.findFetchUserById(UUID.fromString(readStatusId));
    if (readStatus.isPresent() && currentUserId.equals(readStatus.get().getUser().getId())) {
      return new AuthorizationDecision(true);
    }

    return new AuthorizationDecision(false);
  }

}
