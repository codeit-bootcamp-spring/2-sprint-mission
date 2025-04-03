package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final AuthMapper authMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public LoginDTO login(LoginParam loginParam) {
        User user = findUserByUsername(loginParam);
        checkPassword(user, loginParam);
        updateLoginAt(user);
        return authMapper.toLoginDTO(user);
    }

    private User findUserByUsername(LoginParam loginParam) {
        return userRepository.findByUsername(loginParam.username())
                .orElseThrow(() -> {
                    logger.error("로그인 중 유저 찾기 실패: {}", loginParam.username());
                    return RestExceptions.USER_NOT_FOUND;
                });
    }

    private UserStatus findUserStatusByUserId(UUID id) {
        return userStatusService.findByUserId(id);
    }

    private void checkPassword(User user, LoginParam loginParam) {
        if (!BCrypt.checkpw(loginParam.password(), user.getPassword())) {
            // 비밀번호를 틀리는건 예측된 정상 흐름이고, 로깅을 남기는건 보안상 위험하기 때문에 로깅은 남기지 않음
            throw RestExceptions.INVALID_PASSWORD;
        }
    }

    private UserStatus updateLoginAt (User user) {
        UserStatus userStatus = findUserStatusByUserId(user.getId());
        userStatus.updateUserStatus();
        return userStatusService.updateByUserId(user.getId());
    }
}
