package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.repository.JpaUserStatusRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class initializer implements ApplicationRunner {

  private final JpaUserRepository userRepository;
  private final JpaUserStatusRepository userStatusRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (!userRepository.existsByNameOrEmail("admin", "admin@test.com")) {
      String encode = passwordEncoder.encode("admin");
      User admin = User.createAdmin("admin", "admin@test.com", encode, null);
      UserStatus status = UserStatus.create(admin, Instant.now());
      admin.setUserStatus(status);
      userRepository.save(admin);
      userStatusRepository.save(status);
      log.info("어드민 계정 생성 완료");
    }
    if (!userRepository.existsByNameOrEmail("manager", "manager@test.com")) {
      String encode = passwordEncoder.encode("manager");
      User manager = User.createManager("manager", "manager@test.com", encode, null);
      UserStatus status = UserStatus.create(manager, Instant.now());
      manager.setUserStatus(status);
      userRepository.save(manager);
      userStatusRepository.save(status);
      log.info("채널 관리자 계정 생성 완료");
    }
  }
}
