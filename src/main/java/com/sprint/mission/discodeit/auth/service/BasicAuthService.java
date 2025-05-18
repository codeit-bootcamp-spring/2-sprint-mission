package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.exception.AuthPasswordNotMatchException;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Transactional
    @Override
    public UserResult login(LoginRequest loginRequest) {
        User user = validateUserName(loginRequest);
        validatePassword(user, loginRequest.password());
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", user.getId())));

        Instant now = Instant.now();
        userStatus.updateLastActiveAt(now);
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return UserResult.fromEntity(user, savedUserStatus.isOnline(now));
    }

    private User validateUserName(LoginRequest loginRequestUser) {
        return userRepository.findByName(loginRequestUser.username())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userName", loginRequestUser.username())));
    }

    private void validatePassword(User user, String password) {
        if (!user.isSamePassword(password)) {
            throw new AuthPasswordNotMatchException(Map.of("userId", user.getId()));
        }
    }

}
