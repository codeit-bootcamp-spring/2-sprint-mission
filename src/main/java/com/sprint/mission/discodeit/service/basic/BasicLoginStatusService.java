package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.CustomSessionRepository;
import com.sprint.mission.discodeit.service.LoginStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicLoginStatusService implements LoginStatusService {

  private final CustomSessionRepository sessionRepository;

  public boolean isUserOnline(String username) {
    return sessionRepository.existsByPrincipalName(username);
  }
}


