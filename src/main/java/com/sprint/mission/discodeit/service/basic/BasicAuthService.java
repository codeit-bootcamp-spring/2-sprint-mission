package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final PasswordEncoder passwordEncoder;
    @Value("admin")
    private String adminUsername;
    @Value("admin123!")
    private String adminPassword;
    @Value("admin@discodeit.com")
    private String adminEmail;

    private final UserRepository userRepository;

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Transactional
    public void initAdmin() {
        if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(
            adminUsername)) {
            log.warn("이미 admin이 존재합니다.");
            return;
        }
        String encodePassword = passwordEncoder.encode(adminPassword);
        User admin = new User(adminUsername, adminEmail, encodePassword, null);
        admin.updateRole(Role.ROLE_ADMIN);

        userRepository.save(admin);
    }
}
