package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusService {

  private final JpaUserRepository userRepository;

  @Transactional
  public void setUserOnline(UUID userId) {
    userRepository.findById(userId).ifPresent(user -> {
      user.setOnline(true);
      userRepository.save(user);
      log.info("유저 이름 {}: 온라인", user.getName());
    });
  }

  @Transactional
  public void setUserOffline(UUID userId) {
    userRepository.findById(userId).ifPresent(user -> {
      user.setOnline(false);
      userRepository.save(user);
      log.info("유저 이름 {}: 오프라인", user.getName());
    });
  }
}
