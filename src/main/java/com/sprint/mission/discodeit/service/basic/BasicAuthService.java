package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.NotificationEventPublisher;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.SessionInvalidateManager;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final SessionInvalidateManager sessionInvalidateManager;
    private UserMapper userMapper;
    private final JwtSessionRepository jwtSessionRepository;
    private final NotificationEventPublisher notificationEventPublisher;

    @Transactional
    @Override
    public UserDto updateUserRole(RoleUpdateRequest request){
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

        user.setRole(request.newRole());

        userRepository.save(user);

        notificationEventPublisher.publish(new NotificationEvent(
                user.getId(),
                "권한이 변경되었습니다",
                "사용자 권한이 " + request.newRole().name() + "으로 변경되었습니다.",
                NotificationType.ROLE_CHANGED,
                user.getId()
        ));

        // 세션 무효화
        sessionInvalidateManager.invalidateIfPresent(user.getId());

        return userMapper.toDto(user);
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        Optional<JwtSession> sessionOpt = jwtSessionRepository.findByRefreshToken(refreshToken);
        return sessionOpt.map(JwtSession::getAccessToken).orElse(null);
    }
}
