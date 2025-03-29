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
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final AuthMapper authMapper;

    @Override
    public LoginDTO login(LoginParam loginParam) {
        User user = findUserByUsername(loginParam);
        checkPassword(user, loginParam);
        updateLoginAt(user);
        return authMapper.toLoginDTO(user);
    }

    private User findUserByUsername(LoginParam loginParam) {
        return userRepository.findByUsername(loginParam.username())
                .orElseThrow(() -> RestExceptions.USER_NOT_FOUND);
    }

    private UserStatus findUserStatusByUserId(UUID id) {
        return userStatusService.findByUserId(id);
    }

    private void checkPassword(User user, LoginParam loginParam) {
        if (!BCrypt.checkpw(loginParam.password(), user.getPassword())) {
            throw RestExceptions.INVALID_PASSWORD;
        }
    }

    private UserStatus updateLoginAt (User user) {
        UserStatus userStatus = findUserStatusByUserId(user.getId());
        userStatus.updateUserStatus();
        return userStatusService.updateByUserId(user.getId());
    }
}
