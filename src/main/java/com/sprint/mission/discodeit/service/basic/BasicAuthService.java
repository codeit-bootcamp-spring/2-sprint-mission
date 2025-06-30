package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

    @Transactional(readOnly = true)
    @Override
    public UserDto getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return getCurrentUserByUsername(userDetails.getUsername());
    }

    private UserDto getCurrentUserByUsername(String username) {
        log.info("사용자 이름으로 조회: username={}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.withUsername(username));

        log.debug("조회된 사용자 정보: id={}, username={}", user.getId(), user.getUsername());

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUserRole(RoleUpdateRequest request) {
        log.info("사용자 권한 수정 요청: userId={}, newRole={}", request.userId(), request.newRole());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> UserNotFoundException.withId(request.userId()));

        if (user.getRole() != request.newRole()) {
            user.updateRole(request.newRole());
            log.info("권한 변경됨: username={}, newRole={}", user.getUsername(), request.newRole());

            expireUserSessions(user.getUsername());
        }

        return userMapper.toDto(user);
    }

    private void expireUserSessions(String username) {
        log.info("세션 만료 처리 시작: 대상 사용자 = {}", username);

        List<Object> principals = sessionRegistry.getAllPrincipals();

        for (Object principal : principals) {
            if (principal instanceof UserDetails userDetails) {
                if (userDetails.getUsername().equals(username)) {
                    sessionRegistry.getAllSessions(userDetails, false)
                            .forEach(session -> {
                                log.info("세션 종료됨: sessionId={}", session.getSessionId());
                                session.expireNow();
                            });
                }
            }
        }
    }
}
