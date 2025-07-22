package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.NotificationEventPublisher;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    @Value("${ADMIN_USER_NAME}")
    private String adminUsername;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;
    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final NotificationEventPublisher notificationEventPublisher;

    @Transactional
    public UserDto initAdmin() {
        if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(
            adminUsername)) {
            log.warn("이미 admin이 존재합니다.");
            return null;
        }
        String encodePassword = passwordEncoder.encode(adminPassword);
        User admin = new User(adminUsername, adminEmail, encodePassword, null);
        admin.updateRole(Role.ADMIN);

        userRepository.save(admin);

        UserDto adminDto = userMapper.toDto(admin);
        log.info("어드민이 초기화 되었습니다. {}", adminDto);

        return adminDto;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public UserDto updateRole(RoleUpdateRequest request) {
        UUID userId = request.userId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserNotFoundException.withId(userId));
        user.updateRole(request.newRole());

        jwtService.invalidateJwtSession(user.getId());

        notificationEventPublisher.publish(new NotificationEvent(
            user.getId(),
            "권한이 변경되었습니다",
            "새로운 권한: " + request.newRole().name(),
            NotificationType.ROLE_CHANGED,
            user.getId()
        ));

        return userMapper.toDto(user);
    }

}
