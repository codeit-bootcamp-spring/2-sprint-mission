package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDTO login(LoginParam loginParam) {
        User user = findUserByUsername(loginParam);
        checkPassword(user, loginParam);
        UserStatus userStatus = updateLoginAt(user);
        return UserMapper.userEntityToDTO(user, userStatus);
    }

    private User findUserByUsername(LoginParam loginParam) {
        return userRepository.findByUsername(loginParam.username())
                .orElseThrow(() -> RestExceptions.USER_NOT_FOUND);
    }

    private UserStatus findUserStatusByUserId(UUID id) {
        return userStatusRepository.findByUserId(id)
                .orElseThrow(() -> RestExceptions.USER_STATUS_NOT_FOUND);
    }

    private void checkPassword(User user, LoginParam loginParam) {
        if (!BCrypt.checkpw(loginParam.password(), user.getPassword())) {
            throw RestExceptions.INVALID_PASSWORD;
        }
    }

    private UserStatus updateLoginAt (User user) {
        UserStatus userStatus = findUserStatusByUserId(user.getId());
        userStatus.updateUserStatus();
        return userStatusRepository.save(userStatus);
    }
}
