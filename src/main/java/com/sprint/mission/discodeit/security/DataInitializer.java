package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminPassword;

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.findByUsername(adminUsername).isEmpty()) {
      String password = passwordEncoder.encode(adminPassword);
      User user = new User(adminUsername, adminEmail, password, null);
      user.updateRole(Role.ADMIN);
      userRepository.save(user);
    }
  }
}
