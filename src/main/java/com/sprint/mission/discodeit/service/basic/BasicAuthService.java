package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final PersistentTokenRepository persistentTokenRepository;
    @Value("${ADMIN_USER_NAME}")
    private String adminUsername;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;
    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    private final UserRepository userRepository;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            String username = auth.getName();
            persistentTokenRepository.removeUserTokens(username);
        }
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
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
