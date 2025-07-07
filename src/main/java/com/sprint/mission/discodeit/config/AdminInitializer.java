package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// AdminInitializer.java
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    String adminUsername = "admin";
    if (!userRepository.existsByUsername(adminUsername)) {
      User admin = new User(adminUsername, adminUsername, passwordEncoder.encode(adminUsername),
          null);
      admin.setRoles(Role.ROLE_ADMIN.name());
      userRepository.save(admin);
    }
  }
}

