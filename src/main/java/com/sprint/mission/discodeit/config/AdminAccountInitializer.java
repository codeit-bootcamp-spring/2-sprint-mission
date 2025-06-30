package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeAdminAccount() {
        String adminUsername = "admin";
        String adminEmail = "admin@admin.com";
        String adminPassword = "admin123$";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User(
                    adminUsername,
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    null
            );
            admin.updateRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("초기 관리자 계정 생성 완료: username={}", adminUsername);
        } else {
            log.info("관리자 계정 이미 존재함: username={}", adminUsername);
        }
    }
}
